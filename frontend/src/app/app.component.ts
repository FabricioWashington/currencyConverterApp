import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { MoedaDTO } from './model/moeda.model';
import { ConversaoRespostaDTO } from './model/conversao-resposta.model';
import { CotacaoDTO } from './model/cotacao';
import { CurrencyService } from './services/currency.service';

type HistoricoItem = {
  co_moeda_origem: string;
  co_moeda_destino: string;
  vl_montante: number;
  vl_resultado: number;
  vl_taxa: number;
  dt: string;
};

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  private fb = inject(FormBuilder);
  private api = inject(CurrencyService);

  titulo = 'Conversor de Moedas';
  moedas: MoedaDTO[] = [];
  cotacoes: CotacaoDTO[] = [];
  resultado?: ConversaoRespostaDTO;
  historico: HistoricoItem[] = [];
  carregando = false;
  erro?: string;

  form = this.fb.group({
    co_moeda_origem: ['USD', Validators.required],
    co_moeda_destino: ['BRL', Validators.required],
    vl_montante: [1, [Validators.required, Validators.min(0)]],
  });

  private LS_KEY = 'conv_history_pt';

  ngOnInit(): void {
    const hist = localStorage.getItem(this.LS_KEY);
    this.historico = hist ? JSON.parse(hist) : [];

    this.api.listarMoedasAtivas().subscribe({
      next: (res) => {
        this.moedas = (res?.data ?? []).filter(m => m.in_ativo);
        this.carregarCotacoes();
      },
      error: () => { this.moedas = []; }
    });
  }

  inverter() {
    const o = this.form.value.co_moeda_origem;
    const d = this.form.value.co_moeda_destino;
    this.form.patchValue({ co_moeda_origem: d!, co_moeda_destino: o! });
    this.carregarCotacoes();
  }

  carregarCotacoes() {
    const base = this.form.value.co_moeda_origem!;
    if (!base) return;
    this.api.listarUltimasTaxasPorBase(base).subscribe({
      next: (res) => this.cotacoes = res?.data ?? [],
      error: () => this.cotacoes = []
    });
  }

  converter() {
    this.erro = undefined;
    if (this.form.invalid) {
      this.erro = 'Preencha os campos corretamente.';
      return;
    }
    const { co_moeda_origem, co_moeda_destino, vl_montante } = this.form.value as any;
    this.carregando = true;

    this.api.converter(co_moeda_origem, co_moeda_destino, vl_montante).subscribe({
      next: (res) => {
        this.carregando = false;
        this.resultado = res?.data as ConversaoRespostaDTO;

        const item: HistoricoItem = {
          co_moeda_origem: this.resultado.co_moeda_origem,
          co_moeda_destino: this.resultado.co_moeda_destino,
          vl_montante: this.resultado.vl_montante,
          vl_resultado: this.resultado.vl_resultado,
          vl_taxa: this.resultado.vl_taxa,
          dt: new Date().toISOString()
        };
        this.historico.unshift(item);
        this.historico = this.historico.slice(0, 10);
        localStorage.setItem(this.LS_KEY, JSON.stringify(this.historico));

        this.carregarCotacoes();
      },
      error: () => {
        this.carregando = false;
        this.erro = 'Falha na conversão.';
      }
    });
  }

  limparHistorico() {
    this.historico = [];
    localStorage.removeItem(this.LS_KEY);
  }
}

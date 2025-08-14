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
  dt: string; // ISO
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

  dt_cotacao_usada?: string;

  form = this.fb.group({
    vl_montante: [1, [Validators.required, Validators.min(0)]],
    co_moeda_origem: ['BRL', Validators.required],
    co_moeda_destino: ['USD', Validators.required],
    dt_referencia: [new Date().toISOString().slice(0, 10)]
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

  get moedaOrigem(): MoedaDTO | undefined {
    return this.moedas.find(m => m.co_moeda === this.form.value.co_moeda_origem);
  }
  get moedaDestino(): MoedaDTO | undefined {
    return this.moedas.find(m => m.co_moeda === this.form.value.co_moeda_destino);
  }

  inverter() {
    const { co_moeda_origem, co_moeda_destino } = this.form.value;
    this.form.patchValue({
      co_moeda_origem: co_moeda_destino!,
      co_moeda_destino: co_moeda_origem!
    });
    this.dt_cotacao_usada = undefined;
    this.carregarCotacoes();
  }

  atualizar() {
    this.dt_cotacao_usada = undefined;
    this.resultado = undefined;
    this.carregarCotacoes();
  }

  carregarCotacoes() {
    const base = this.form.value.co_moeda_origem!;
    if (!base) return;

    this.api.listarUltimasTaxasPorBase(base).subscribe({
      next: (res) => {
        this.cotacoes = res?.data ?? [];
        // se já há destino selecionado, pegue a data da cotação correspondente (se existir)
        const dest = this.form.value.co_moeda_destino!;
        const ct = this.cotacoes.find(c => c.co_moeda_cotada === dest);
        this.dt_cotacao_usada = ct?.dt_referencia;
      },
      error: () => this.cotacoes = []
    });
  }

  get taxaDireta(): number | null {
    if (this.resultado?.vl_taxa) return this.resultado.vl_taxa;
    const dest = this.form.value.co_moeda_destino!;
    const c = this.cotacoes.find(ct => ct.co_moeda_cotada === dest);
    return c?.vl_taxa ?? null;
  }
  get taxaInversa(): number | null {
    const t = this.taxaDireta;
    return t ? 1 / t : null;
  }

  converter() {
    this.erro = undefined;
    if (this.form.invalid) {
      this.erro = 'Preencha os campos corretamente.';
      return;
    }

    const { co_moeda_origem, co_moeda_destino, vl_montante, dt_referencia } = this.form.value as any;
    this.carregando = true;

    // conversão (endpoint atual não recebe data; usamos data nas exibições)
    this.api.converter(co_moeda_origem, co_moeda_destino, vl_montante).subscribe({
      next: (res) => {
        this.carregando = false;
        this.resultado = res?.data as ConversaoRespostaDTO;

        // tenta obter a cotação na data informada (se o backend suportar via /rates/search)
        this.buscarCotacaoNaData(co_moeda_origem, co_moeda_destino, dt_referencia);

        // histórico
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
      },
      error: () => {
        this.carregando = false;
        this.erro = 'Falha na conversão.';
      }
    });
  }

  private buscarCotacaoNaData(base: string, dest: string, dataISO: string) {
    const filtro = {
      page: 0,
      pageSize: 1,
      search: '',
      data: {
        base: base,
        quote: dest,
        asOfDate: dataISO
      }
    };

    this.api.buscarCotacoes(filtro).subscribe({
      next: (res) => {
        const lista = res?.data ?? [];
        if (lista.length) {
          this.dt_cotacao_usada = lista[0].dt_referencia;
        } else if (!this.dt_cotacao_usada) {
          this.dt_cotacao_usada = dataISO;
        }
      },
      error: () => {
        if (!this.dt_cotacao_usada) this.dt_cotacao_usada = dataISO;
      }
    });
  }

  limparHistorico() {
    this.historico = [];
    localStorage.removeItem(this.LS_KEY);
  }
}

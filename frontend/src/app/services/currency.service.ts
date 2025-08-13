import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { BaseService } from '../web/shared/services/base.service';
import { environment } from '../../../environment';
import { IResponseListJson } from '../web/shared/response/response-list-json';
import { IResponseSuccessJson } from '../web/shared/response/response-success-json';
import { MoedaDTO } from '../model/moeda.model';
import { CotacaoDTO } from '../model/cotacao';
import { ConversaoRespostaDTO } from '../model/conversao-resposta.model';

@Injectable({ providedIn: 'root' })
export class CurrencyService extends BaseService<any> {
  override endpoint = `/currency`;
  override api = environment.api;

  constructor(http: HttpClient) {
    super(http, environment);
  }

  listarMoedasAtivas() {
    return this.http.get<IResponseListJson<MoedaDTO[]>>(
      `${this.api}${this.endpoint}/symbols`
    );
  }

  listarUltimasTaxasPorBase(co_moeda_base: string) {
    return this.http.get<IResponseListJson<CotacaoDTO[]>>(
      `${this.api}${this.endpoint}/rates/${co_moeda_base}`
    );
  }

  converter(co_moeda_origem: string, co_moeda_destino: string, vl_montante: number) {
    const params = new HttpParams()
      .set('from', co_moeda_origem)
      .set('to', co_moeda_destino)
      .set('amount', String(vl_montante));

    return this.http.get<IResponseSuccessJson<ConversaoRespostaDTO>>(
      `${this.api}${this.endpoint}/convert`,
      { params }
    );
  }

  buscarCotacoes(filtro: any) {
    return this.http.post<IResponseListJson<CotacaoDTO[]>>(
      `${this.api}${this.endpoint}/rates/search`,
      filtro
    );
  }

  semearDados() {
    return this.http.post<IResponseSuccessJson<void>>(
      `${this.api}${this.endpoint}/seed`, {}
    );
  }
}

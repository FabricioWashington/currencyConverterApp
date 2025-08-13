export interface CotacaoDTO {
  id_fonte_cotacao: number;
  co_moeda_base: string;
  co_moeda_cotada: string;
  vl_taxa: number;
  dt_referencia: string;
  dt_coleta: string;
}

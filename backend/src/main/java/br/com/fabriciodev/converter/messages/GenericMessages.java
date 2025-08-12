package br.com.fabriciodev.converter.messages;

import java.util.HashMap;
import java.util.Map;

public class GenericMessages {

    private static final Map<String, String> messages = new HashMap<>();

    static {
        messages.put("generic.true", "Ativo");
        messages.put("generic.false", "Inativo");
        messages.put("generic.coTrue", "AT");
        messages.put("generic.coFalse", "IN");
        messages.put("generic.visible", "Mostrar");
        messages.put("generic.invisible", "Esconder");
        messages.put("generic.all", "Todos");
        messages.put("generic.yes", "Sim");
        messages.put("generic.no", "Não");
        messages.put("generic.name", "Nome");
        messages.put("generic.description", "Descrição");
        messages.put("generic.format", "Formato");
        messages.put("generic.summary", "Descrição");
        messages.put("generic.infoSearchAdvanced", "Informe as opções de busca");
        messages.put("generic.save", "Salvar");
        messages.put("generic.create", "Cadastrar");
        messages.put("generic.create_title", "Cadastrar novo registro");
        messages.put("generic.update", "Atualizar");
        messages.put("generic.update_title", "Alterar registro");
        messages.put("generic.delete", "Deletar");
        messages.put("generic.delete_title", "Deletar registro");
        messages.put("generic.add", "Adicionar");
        messages.put("generic.addFile", "Adicionar arquivos");
        messages.put("generic.view", "Visualizar");
        messages.put("generic.search", "Pesquisar");
        messages.put("generic.confirm", "Confirmar");
        messages.put("generic.searchForm", "Pesquisa");
        messages.put("generic.searchAdvanced", "Pesquisa Avançada");
        messages.put("generic.searchForm_title", "Visualizar opções de pesquisa");
        messages.put("generic.number", "Número");
        messages.put("generic.config", "Configurações");
        messages.put("generic.sigla", "SIGLA");
        messages.put("generic.total", "Total");
        messages.put("generic.totalGeral", "Total geral");
        messages.put("generic.totalDemandas", "Total de demandas");
        messages.put("generic.codigo", "Código");
        messages.put("generic.edit", "Editar");
        messages.put("generic.back", "Voltar");
        messages.put("generic.clear", "Limpar");
        messages.put("generic.login", "Acessar");
        messages.put("generic.cancel", "Cancelar");
        messages.put("generic.close", "Fechar");
        messages.put("generic.action", "Ações");
        messages.put("generic.saveApprove", "Aprovar");
        messages.put("generic.saveFinalize", "Finalizar");
        messages.put("generic.saveApproveAll", "Aprovar todos");
        messages.put("generic.criacao", "Criação");
        messages.put("generic.print", "Imprimir");
        messages.put("generic.regiVinculados", "Registros Vinculados");
        messages.put("generic.campoObrigatorio", "Campos de preenchimento obrigatório.");
        messages.put("generic.camposObrigatorio", "Campos com {n} são obrigatórios.");
        messages.put("generic.selecione", "Selecione {n}");
        messages.put("generic.selecioneUm", "Selecione um {n}");
        messages.put("generic.selecioneUma", "Selecione uma {n}");
        messages.put("generic.sucesso", "Informações salvas com sucesso!");
        messages.put("generic.erro", "Erro ao execução a ação!");
        messages.put("generic.sucessoCancelado", "Item cancelado com sucesso!");
        messages.put("generic.removido", "A exclusão do item foi realizada com sucesso!");
        messages.put("generic.cancelado", "O item foi cancelado com sucesso!");
        messages.put("generic.integrado", "Integrado");
        messages.put("generic.naointegrado", "Não Integrado");
        messages.put("generic.periodo", "Período");
        messages.put("generic.mes", "Mês");
        messages.put("generic.mesInicio", "Mês Início");
        messages.put("generic.dt_inicio", "Dt. Início");
        messages.put("generic.dt_final", "Dt. Final");
        messages.put("generic.tipo_pesquisa", "Tipo Pesquisa");
        messages.put("generic.origem_etiqueta", "Origem");
    }

    public static String get(String key) {
        return messages.getOrDefault(key, key);
    }
}

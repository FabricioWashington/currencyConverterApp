package br.com.fabriciodev.converter.dto.retornoJson;

public class RetornoErroJsonDTO {
    private String mensagem;

    public RetornoErroJsonDTO(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}

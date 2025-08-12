package br.com.fabriciodev.converter.dto.retornoJson;

public class RetornoSucessoJsonDTO {
    private String mensagem;
    private Object data;

    public RetornoSucessoJsonDTO(String mensagem, Object data) {
        this.mensagem = mensagem;
        this.data = data;
    }

    public RetornoSucessoJsonDTO(String mensagem) {
        this(mensagem, null);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package br.com.fabriciodev.converter.dto.retornoJson;

public class RetornoListaJsonDTO {
    private Integer totalRegistros;
    private Object data;

    public RetornoListaJsonDTO(Object data, Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
        this.data = data;
    }

    public RetornoListaJsonDTO(Object data) {
        this(data, null);
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

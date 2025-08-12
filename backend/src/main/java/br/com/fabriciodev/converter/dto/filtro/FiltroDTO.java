package br.com.fabriciodev.converter.dto.filtro;

import java.util.Map;

import lombok.Data;

@Data
public class FiltroDTO {

    private String search;
    private Integer page;
    private Integer pageSize;
    private String sortField;
    private String sortOrder;
    private Map<String, Object> data;

    public FiltroDTO() {
    }

    public FiltroDTO(Map<String, Object> request) {
        this.search = (String) request.getOrDefault("search", null);
        this.page = request.get("page") != null ? Integer.parseInt(request.get("page").toString()) : null;
        this.pageSize = request.get("pageSize") != null ? Integer.parseInt(request.get("pageSize").toString()) : null;
        this.sortField = (String) request.getOrDefault("sortField", null);
        this.sortOrder = (String) request.getOrDefault("sortOrder", null);
        this.data = (Map<String, Object>) request.getOrDefault("data", null);
    }
}

package br.com.fabriciodev.converter.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoErroJsonDTO;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public abstract class BaseApiController {

    @Autowired
    protected HttpServletRequest request;

    protected Map<String, Object> getRequest() throws IOException {
        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            Map<String, Object> params = new HashMap<>();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String param = parameterNames.nextElement();
                params.put(param, request.getParameter(param));
            }
            return params;
        } else {
            String json = getContent();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Map.class);
        }
    }

    protected FiltroDTO getFiltro() throws IOException {
        Map<String, Object> req = getRequest();
        FiltroDTO filtro = new FiltroDTO(req);
        filtro.setPage(filtro.getPage() != null ? filtro.getPage() : 0);
        filtro.setPageSize(filtro.getPageSize() != null ? filtro.getPageSize() : 1000);
        return filtro;
    }

    protected String getAuthorization() {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isEmpty()) {
            throw new RuntimeException("Token não informado!");
        }
        return auth;
    }

    protected Map<String, Object> toObject(Map<String, Object> map) {
        return map;
    }

    protected String getContent() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For", "X-Real-IP", "CF-Connecting-IP",
                "X-Client-IP", "Forwarded"
        };
        for (String h : headers) {
            String v = request.getHeader(h);
            if (v != null && !v.isBlank()) {
                return v.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    protected ResponseEntity<Object> error(Exception ex) {
        String message = ex.getMessage();
        int code = message != null && message.contains("Por favor, corrija os seguintes erros") ? 400 : 500;
        return ResponseHandler.renderJSON(new RetornoErroJsonDTO(message), code);
    }
}

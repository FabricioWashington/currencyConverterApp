package br.com.fabriciodev.converter.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fabriciodev.converter.components.XDebug;
import br.com.fabriciodev.converter.messages.FieldLabelResolver;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> erros = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            Map<String, String> erro = new HashMap<>();

            String campoTecnico = fieldError.getField();
            String campoAmigavel = FieldLabelResolver.get(campoTecnico);

            erro.put("campo", campoAmigavel);
            erro.put("campoTecnico", campoTecnico);
            erro.put("mensagem", fieldError.getDefaultMessage());

            erros.add(erro);
        }

        Map<String, Object> resposta = new LinkedHashMap<>();
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("mensagem", "Erro de validação");
        resposta.put("erros", erros);

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(XDebug.XDebugInterruptException.class)
    public ResponseEntity<?> ignorarInterrupcaoDebug() {
        return null;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
    }
}

package br.com.fabriciodev.converter.components;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static <T> ResponseEntity<T> renderJSON(T data) {
        return ResponseEntity.ok(data);
    }

    public static <T> ResponseEntity<T> renderJSON(T data, int status) {
        HttpStatus httpStatus = HttpStatus.resolve(status);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(data, httpStatus);
    }
}
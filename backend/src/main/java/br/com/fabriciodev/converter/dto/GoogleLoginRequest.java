package br.com.fabriciodev.converter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginRequest {
    private String idToken;
    private String email;
    private String name;
    private String id;
}
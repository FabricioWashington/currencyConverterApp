package br.com.fabriciodev.converter.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabriciodev.converter.components.BaseApiController;
import br.com.fabriciodev.converter.dto.GoogleLoginRequest;
import br.com.fabriciodev.converter.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseApiController {

    private final UsuarioService service;

    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            return ResponseEntity.ok(service.autenticarComGoogle(request));
        } catch (Exception e) {
            return error(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(service.authenticateUser(request));
        } catch (Exception e) {
            return error(e);
        }
    }

}

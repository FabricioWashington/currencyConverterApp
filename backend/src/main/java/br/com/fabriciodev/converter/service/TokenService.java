package br.com.fabriciodev.converter.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fabriciodev.converter.config.JwtProperties;
import br.com.fabriciodev.converter.model.VwAcessos;
import io.jsonwebtoken.*;

@Service
public class TokenService {

    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;

    public TokenService(JwtProperties jwtProperties, PasswordEncoder passwordEncoder) {
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

    public String gerarToken(VwAcessos usuario) {
        return Jwts.builder()
                .setSubject(usuario.getIdUsuario().toString())
                .claim("idEmpresa", usuario.getIdEmpresa())
                .claim("noFantasia", usuario.getNoFantasia())
                .claim("noRazaoSocial", usuario.getNoRazaoSocial())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("noUsuario", usuario.getNoUsuario())
                .claim("inAtivo", usuario.getInAtivo())
                .claim("idPerfil", usuario.getIdPerfil())
                .claim("dsPerfilCurto", usuario.getDsPerfilCurto())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret().getBytes())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
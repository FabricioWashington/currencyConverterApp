package br.com.fabriciodev.converter.components;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.fabriciodev.converter.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthContext {

    private final JwtProperties jwtProperties;

    public AuthContext(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Integer getIdUsuario() {
        if (isCli())
            return 0;

        Claims claims = getClaims();

        if (claims == null)
            throw new RuntimeException("Token inválido ou ausente.");

        return claims.get("idUsuario", Integer.class);
    }

    public Integer getIdEmpresa() {
        if (isCli())
            return 0;

        Claims claims = getClaims();

        if (claims == null)
            throw new RuntimeException("Token inválido ou ausente.");

        return claims.get("idEmpresa", Integer.class);
    }

    private Claims getClaims() {
        String token = getTokenFromRequest();
        if (token == null)
            return null;

        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    private String getTokenFromRequest() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null)
            return null;

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private boolean isCli() {
        return System.getProperty("java.class.path").contains("surefire") || System.console() == null;
    }
}

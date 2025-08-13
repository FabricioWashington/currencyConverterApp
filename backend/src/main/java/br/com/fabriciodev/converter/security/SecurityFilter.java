package br.com.fabriciodev.converter.security;

import br.com.fabriciodev.converter.service.TokenService;
import br.com.fabriciodev.converter.model.VwAcessos;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import br.com.fabriciodev.converter.repository.VwAcessosRepository;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final VwAcessosRepository vwAcessosRepository;

    public SecurityFilter(@Lazy TokenService tokenService, VwAcessosRepository vwAcessosRepository) {
        this.tokenService = tokenService;
        this.vwAcessosRepository = vwAcessosRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            try {
                Claims claims = tokenService.parseToken(token);
                Integer idUsuario = Integer.valueOf(claims.getSubject());

                VwAcessos usuario = vwAcessosRepository.findFirstByIdUsuario(idUsuario).orElse(null);

                if (usuario != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

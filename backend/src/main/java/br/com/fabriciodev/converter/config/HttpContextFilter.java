package br.com.fabriciodev.converter.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import br.com.fabriciodev.util.HttpContext;

import java.io.IOException;

@Component
public class HttpContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpContext.setResponse((HttpServletResponse) response);
            chain.doFilter(request, response);
        } finally {
            HttpContext.clear();
        }
    }
}

package br.com.fabriciodev.util;

import jakarta.servlet.http.HttpServletResponse;

public class HttpContext {
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();

    public static void setResponse(HttpServletResponse response) {
        responseHolder.set(response);
    }

    public static HttpServletResponse getResponse() {
        return responseHolder.get();
    }

    public static void clear() {
        responseHolder.remove();
    }
}

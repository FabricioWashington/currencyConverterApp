package br.com.fabriciodev.converter.components;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import br.com.fabriciodev.util.HttpContext;

public class XDebug {

    public static void xd(Object... args) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        StringBuilder html = new StringBuilder();
        html.append("<div style='border: 1px solid black; padding: 10px; background-color: #BBCCDD'>");
        html.append("Arquivo: ").append(caller.getFileName())
                .append(" ---> Linha ").append(caller.getLineNumber()).append("<br><p>");

        for (int i = 0; i < args.length; i++) {
            html.append("<b><u>ARG[").append(i).append("]</u></b><br>")
                    .append("<pre style='white-space: pre-wrap; word-break: break-word;'>")
                    .append(format(args[i]))
                    .append("</pre>");
        }

        html.append("</div><br><br>");

        HttpServletResponse response = HttpContext.getResponse();
        if (response == null) {
            throw new RuntimeException("XDebug.xd() não pode acessar a resposta HTTP.");
        }

        try {
            response.setStatus(500);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(html.toString());
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever resposta no XDebug.xd()", e);
        }

        throw new XDebugInterruptException();
    }

    private static String format(Object obj) {
        if (obj == null)
            return "null";
        if (obj.getClass().isArray())
            return Arrays.deepToString((Object[]) obj);
        return obj.toString();
    }

    public static class XDebugInterruptException extends RuntimeException {
    }
}
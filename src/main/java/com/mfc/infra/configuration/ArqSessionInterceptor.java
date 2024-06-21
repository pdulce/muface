package com.mfc.infra.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ArqSessionInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<HttpSession> currentSession = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // Recuperar valores de la sesión desde los headers
        String sessionId = request.getHeader("X-Session-Id");
        String traceId = request.getHeader("X-Trace-Id");
        String applicationId = request.getHeader("X-Application-Id");

        // Almacenar los valores en la sesión
        if (sessionId != null) {
            session.setAttribute("sessionId", sessionId);
        }
        if (traceId != null) {
            session.setAttribute("traceId", traceId);
        }
        if (applicationId != null) {
            session.setAttribute("applicationId", applicationId);
        }

        // Set the session in ThreadLocal
        currentSession.set(session);

        // Continuar con el procesamiento de la solicitud
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        currentSession.remove();
    }

    public static HttpSession getCurrentSession() {
        return currentSession.get();
    }
}


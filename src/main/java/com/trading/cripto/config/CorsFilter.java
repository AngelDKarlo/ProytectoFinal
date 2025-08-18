package com.trading.cripto.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", 
            "Authorization, Content-Type, X-Requested-With, Accept, Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.setHeader("Access-Control-Allow-Credentials", "false");
        response.setHeader("Access-Control-Max-Age", "3600");

        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        if (uri.contains("/market/prices")) {
            System.out.println("🔄 [CORS Filter] MARKET/PRICES: " + method + " " + uri);
        }
        
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("✅ [CORS Filter] OPTIONS request para " + uri + " - respondiendo con 200");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🚀 [CORS Filter] Inicializado - CORS habilitado");
    }

    @Override
    public void destroy() {
        System.out.println("🛑 [CORS Filter] Destruido");
    }
}

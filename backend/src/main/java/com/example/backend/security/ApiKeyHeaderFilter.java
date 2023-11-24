package com.example.backend.security;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class ApiKeyHeaderFilter implements Filter {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String apiKeyHeader = ((HttpServletRequest) request).getHeader("x-api-key");
            var keys = apiKeyRepository.findByValue(apiKeyHeader);
            if (keys.size() > 0) {
                var key = keys.get(0);

                ApiKeyAuthenticationToken apiToken = new ApiKeyAuthenticationToken(key.getValue(), key.getName(),key.getRoles());
                SecurityContextHolder.getContext().setAuthentication(apiToken);
                chain.doFilter(request, response);
            } else {
                System.out.println("bad api key");
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpResponse.getWriter().write("Invalid API key");
            }
        } catch (Exception e) {
            System.out.println(e);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("Missing x-api-key");
        }

    }

}

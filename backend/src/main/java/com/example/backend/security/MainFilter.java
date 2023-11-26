package com.example.backend.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.backend.security.session.SessionRepository;
import com.example.backend.security.user.UserRepository;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MainFilter implements Filter {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String sessionId = ((HttpServletRequest) request).getSession(false).getId();
            var maybeSessionEntity = sessionRepository.findByJSESSIONID(sessionId);
            if (maybeSessionEntity == null){
                chain.doFilter(request, response);
                return;
            }

            var sessionEntity = sessionRepository.findFirstByuserIdOrderByCreatedDateTimeDesc(maybeSessionEntity.getUserId());
            if (sessionEntity == null) {
                chain.doFilter(request, response);
                return;
            }
            var user = userRepository.findById(sessionEntity.getUserId()).get();

            SecurityContextHolder.getContext().setAuthentication(new UserToken(user.getUsername(), user.getRoles()));

            chain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println(e);
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("Not allowed!");
        }

    }
}

package com.ibrahimeshag.basicspringsecutiywithjdbcuserdetailsmanager.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class StaticKeyAuthenticationFilter
        implements Filter {

    @Value("${authorization.key}")
    private String authorizationKey;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationLoggingFilter.class);


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        String authentication =
                httpRequest.getHeader("Authorization");

        logger.info("Authorization header: " + authentication);
        logger.info("Authorization key: " + authorizationKey);


        if (authorizationKey.equals(authentication)) {
            filterChain.doFilter(request, response);
        } else {
            httpResponse.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
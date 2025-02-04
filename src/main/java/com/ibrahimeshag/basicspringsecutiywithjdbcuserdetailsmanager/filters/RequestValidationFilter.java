package com.ibrahimeshag.basicspringsecutiywithjdbcuserdetailsmanager.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RequestValidationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestValidationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        logger.info("welcome to the request validation filter...");
        logger.info("Request URI: " + httpRequest.getRequestURI());
        logger.info("IP Address: " + httpRequest.getRemoteAddr());

        String requestId = httpRequest.getHeader("Request-Id");
        logger.info("Request-Id: " + requestId);
        if (requestId == null || requestId.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        chain.doFilter(request, response);
    }
}

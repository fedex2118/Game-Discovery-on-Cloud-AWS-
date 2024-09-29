package com.gateway.gateway_api.custom.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class works with MDC and in synchronous way
 */
public class TraceIdLoggingFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String traceId = httpServletRequest.getHeader("X-Trace-Id");
        // generate trace id if it doesn't exist
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            httpServletRequest.setAttribute("X-Trace-Id", traceId);
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("X-Trace-Id", traceId);
        // set in MDC for logging within this request's context
        MDC.put("traceId", traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("traceId"); // clear after the request is finished processing
        }
    }
}

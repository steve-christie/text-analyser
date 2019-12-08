package com.christie.textanalyzer.config;

import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class CommonsRequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return logger.isInfoEnabled();
    }

    /**
     * Writes a log message before the request is processed.
     */
    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        //do nothing
    }

    /**
     * Writes a log message after the request is processed.
     */
    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(String.format("%s | %s", request.getMethod(), message));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        List<String> excludeUrlPatterns = Collections.singletonList("/application/*");

        AntPathMatcher pathMatcher = new AntPathMatcher();
        return excludeUrlPatterns.stream()
            .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }
}

package com.christie.textanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestLoggingFilterConfig {

    @Value("${com.volocommerce.web.config.logging.max_payload_length:1000}")
    private String maxPayloadLength;

    @Value("${com.volocommerce.web.config.logging.include_payload:true}")
    private boolean includePayload;

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(includePayload);
        filter.setIncludeClientInfo(true);
        filter.setMaxPayloadLength(Integer.parseInt(maxPayloadLength));
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}

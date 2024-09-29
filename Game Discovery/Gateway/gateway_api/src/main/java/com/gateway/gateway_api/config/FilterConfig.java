package com.gateway.gateway_api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.gateway.gateway_api.custom.logging.TraceIdLoggingFilter;

@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<TraceIdLoggingFilter> loggingFilter(){
        FilterRegistrationBean<TraceIdLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceIdLoggingFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}

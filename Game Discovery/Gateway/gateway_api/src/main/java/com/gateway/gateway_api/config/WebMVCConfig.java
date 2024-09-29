package com.gateway.gateway_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gateway.gateway_api.custom.interceptors.AuthenticationInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**") // Specify the patterns to intercept
                .excludePathPatterns("/swagger-ui/**", // exclude Swagger UI 
                		"/v3/api-docs/**", // excude API docs
                		"/api/v1/auth/**", // exclude auth
                		"/api/v1/token/**"); // exclude refresh-token
    }
}

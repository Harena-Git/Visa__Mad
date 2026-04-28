package com.visa.backoffice.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

// @Configuration
public class TomcatConfig {

    // @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherServlet(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(dispatcherServlet);
        registration.addUrlMappings("/");
        registration.setName("dispatcherServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}

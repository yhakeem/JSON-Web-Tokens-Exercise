package com.example.jfs_jwt;

import com.example.jfs_jwt.config.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JfsJwtApplication {

    public static void main(String[] args) {SpringApplication.run(JfsJwtApplication.class, args);

    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter()
    {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter( new JwtFilter() );
        registrationBean.addUrlPatterns( "/api/*" );
        return registrationBean;
    }



}

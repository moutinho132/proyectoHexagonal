package com.martzatech.vdhg.crmprojectback.infrastructure.configs;

import net.kaczmarzyk.spring.data.jpa.swagger.springdoc.SpecificationArgResolverSpringdocOperationCustomizer;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new SpecificationArgumentResolver());
  }

  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(
            "http://localhost",
            "http://localhost:3000",
            "http://martza-tech-crm-frontend-dev.azurewebsites.net/",
            "https://martza-tech-crm-frontend-dev.azurewebsites.net/",
            "http://martza-tech-crm-frontend-test.azurewebsites.net/",
            "https://martza-tech-crm-frontend-test.azurewebsites.net/",
            "http://martza-tech-crm-frontend-staging.azurewebsites.net",
            "https://martza-tech-crm-frontend-staging.azurewebsites.net"
        )
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
        .allowCredentials(true);

  }

    @Bean
    public SpecificationArgResolverSpringdocOperationCustomizer specificationArgResolverSpringdocOperationCustomizer() {
        return new SpecificationArgResolverSpringdocOperationCustomizer();
    }
}

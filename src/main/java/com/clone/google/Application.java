package com.clone.google;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@SpringBootApplication
public class Application {

    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        TemplateResolver resolver = new FileTemplateResolver();
        resolver.setSuffix(".html");
        resolver.setPrefix("src/main/resources/templates/");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        return resolver;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package de.oryfox.vinylish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.field}")
    private String field;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("Cors Mapping: " + field);
        if (!field.equals("."))
            registry.addMapping("/**")
                    .allowedOrigins(field)
                    .allowedMethods(field)
                    .allowedHeaders(field)
                    .allowCredentials(false).maxAge(3600);
    }
}

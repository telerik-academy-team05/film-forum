package com.telerik.filmforum.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI filmForumOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Film Forum API")
                        .description("REST API for Film Forum. Users can create posts, comment, like content, and admins can manage users.")
                        .version("1.0"));
    }
}
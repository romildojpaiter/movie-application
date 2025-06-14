package com.outsera.movie.core;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
      title = "API Movies",
      description = "Application to manage movies"),
    servers = {
        @Server(url = "http://localhost:8080/movie", description = "local"),
    }
)
public class OpenApiConfig {

}
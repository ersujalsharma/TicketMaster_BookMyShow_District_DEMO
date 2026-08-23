package com.sujal.ticketmaster.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketmasterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TicketMaster API")
                        .description("TicketMaster / Book My Show - Event Booking System API")
                        .version("1.0.0"));
    }
}

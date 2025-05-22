package com.eucl.eucl_management_system.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("EUCL Token Management System")
                        .description("API documentation for the EUCL Token Management System. This API allows users to manage tokens, meters, notifications, and user profiles.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("EUCL Support Team")
                                .email("support@eucl.com")
                        )
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("eucl-public")
                .pathsToMatch("/api/**")
                .displayName("EUCL API")
                .packagesToScan("com.eucl.eucl_management_system")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    if (operation.getTags() != null) {
                        operation.setTags(
                                operation.getTags().stream()
                                        .map(this::simplifyTag)
                                        .toList()
                        );
                    }
                    return operation;
                })
                .build();
    }

    private String simplifyTag(String tag) {
        return tag.replace("Controller", "");
    }
}
package nl.rgs.kib.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi internalGroup() {
        return GroupedOpenApi.builder()
                .group("internal")
                .pathsToExclude("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://kib.service.rgsplus.nl");
        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("Kib Application API")
                        .version("1.0")
                        .description("API documentation for Kib Application"));
    }
}

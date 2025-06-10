package com.seed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Seed IoT Platform API")
                        .description("Seed平台 API 文档 - 提供设备管理、用户管理、语音识别、TTS等功能的RESTful API接口")
                        .version("3.3.0")
                        .contact(new Contact()
                                .name("Seed Team")
                                .email("support@seed.com")
                                .url("https://github.com/seed-project"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8091")
                                .description("本地开发环境"),
                        new Server()
                                .url("https://api.seed.com")
                                .description("生产环境")
                ));
    }
} 
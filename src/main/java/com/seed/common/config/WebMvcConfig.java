package com.seed.common.config;

import com.seed.common.interceptor.AuthenticationInterceptor;
import com.seed.common.interceptor.LogInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import jakarta.annotation.Resource;

import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LogInterceptor logInterceptor;

    @Resource
    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/api/user/login",
                    "/api/user/register",
                    "/api/device/ota",
                    "/audio/**",
                    "/uploads/**",
                    "/avatar/**",
                    "/ws/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/webjars/**"
                );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // 获取项目根目录的绝对路径
            String basePath = new File("").getAbsolutePath();

            // 音频文件存储在项目根目录下的audio文件夹中
            String audioPath = "file:" + basePath + File.separator + "audio" + File.separator;

            // 配置资源映射
            registry.addResourceHandler("/audio/**")
                    .addResourceLocations(audioPath);

            // 添加 SpringDoc Swagger UI 静态资源映射
            registry.addResourceHandler("/swagger-ui/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
                    
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
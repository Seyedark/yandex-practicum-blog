package ru.yandex.practicum.blog.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.yandex.practicum.blog.test")
@PropertySource("classpath:test-application.properties")
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${images.url.path}")
    String imagesUrlPath;

    @Value("${images.local.path}")
    String imagesLocalPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(imagesUrlPath + "**")
                .addResourceLocations(imagesLocalPath + imagesUrlPath);
    }
}
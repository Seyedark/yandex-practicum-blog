package ru.yandex.practicum.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${images.url.path}")
    String imagesUrlPath;

    @Value("${images.local.path}")
    String imagesLocalPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(imagesUrlPath + "**")
                .addResourceLocations(imagesLocalPath + imagesUrlPath);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
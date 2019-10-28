package com.job.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author keith
 * @version 1.0
 * @date 2019/10/28
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${fileSavePath}")
    private String fileSavePath;

    @Value("${resourcePath}")
    private String resourcePath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath + "/**").addResourceLocations("file:" + fileSavePath);
    }
}

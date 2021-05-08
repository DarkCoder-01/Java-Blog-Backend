package com.darkcoder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author 徐泽爽
 * @version 1.0
 * @date 2021/5/2 15:06
 */
@Configuration
public class FileUploadConfig extends WebMvcConfigurationSupport {
    @Value("${file.file-save-path}")
    private String fileSavePath;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + fileSavePath);
        super.addResourceHandlers(registry);
    }
}

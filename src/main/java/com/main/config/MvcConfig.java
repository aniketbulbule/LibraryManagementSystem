package com.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Value("${app.uploads.dir}")
  private String uploadsDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    Path uploadPath = Paths.get(uploadsDir).toAbsolutePath().normalize();
    String location = "file:" + uploadPath.toString() + "/";
    registry.addResourceHandler("/uploads/**").addResourceLocations(location);
  }
}

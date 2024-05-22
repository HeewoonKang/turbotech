package com.example.articleboard.articleboard.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class boardMVCConfigurator implements WebMvcConfigurer
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/dealers/**")
                .addResourceLocations("file:////home/ec2-user/turbotech/DealerAgent/");

        registry.addResourceHandler("/gallery/**")
                .addResourceLocations("file:////home/ec2-user/turbotech/Gallery/");

        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:////home/ec2-user/turbotech/Media/");

        registry.addResourceHandler("/newsletter/**")
                .addResourceLocations("file:////home/ec2-user/turbotech/Newsletter/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:////home/ec2-user/turbotech/Video/Image/");

        registry.addResourceHandler("/video/**")
                .addResourceLocations("file:////home/ec2-user/turbotech/Video/Video/");
    }
}

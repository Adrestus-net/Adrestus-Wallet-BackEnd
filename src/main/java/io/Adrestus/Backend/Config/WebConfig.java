//package io.Adrestus.Backend.Config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000/")
//                .allowCredentials(false)
//                .maxAge(3600)
//                .allowedHeaders("Accept", "Content-Type", "Origin", "Authorization", "X-Auth-Token")
//                .exposedHeaders("X-Auth-Token", "Authorization")
//                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
//    }
//}

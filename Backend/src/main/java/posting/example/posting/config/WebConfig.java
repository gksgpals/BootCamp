package posting.example.posting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 루트 요청
        registry.addViewController("/").setViewName("forward:/index.html");
        // 마침표(.) 없는 단일 세그먼트 경로 예: /foo
        registry.addViewController("/{spring:[^.]*}")
                .setViewName("forward:/index.html");
        // 마침표(.) 없는 다중 세그먼트 경로 예: /foo/bar, /foo/bar/baz
        registry.addViewController("/**/{spring:[^.]*}")
                .setViewName("forward:/index.html");
    }
}

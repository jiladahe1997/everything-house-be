package hello;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class loginInterceptor implements WebMvcConfigurer {

    @Autowired
    CheckLogin checkLogin;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLogin).addPathPatterns("/**").excludePathPatterns("/qqlogin/**").excludePathPatterns("/error");
    }
}


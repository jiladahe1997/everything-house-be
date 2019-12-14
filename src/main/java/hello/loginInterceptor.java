package hello;


import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class loginInterceptor implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLogin());
    }
}

class CheckLogin implements HandlerInterceptor {

    SqlSessionFactory sqlSessionFactory;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res,Object handler) throws IOException {
        SqlSession session = this.sqlSessionFactory.openSession();
        // 根据token去qq开发平台拿openid
        // 1.如果没有token，则重定向到登录页
        // 2.如果token过期，也重定向到登录页
         List<Cookie> cookies = Arrays.asList(req.getCookies());
         Cookie token = IterableUtils.find(cookies, new Predicate<Cookie>() {
             @Override
             public boolean evaluate(Cookie object) {
                 return object.getName().equals("_j_token");
             }
         });
         if( null == token) {
             res.sendRedirect("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101833914&redirect_uri=https://jiladahe1997.cn/qqlogin/callback");
            return false;
         }
         return  true;

        //session.selectList("org.mybatis.example.sentenceMapper.selectSentence");

    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler,
                              @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler,
                                   @Nullable Exception ex) {
    }
}
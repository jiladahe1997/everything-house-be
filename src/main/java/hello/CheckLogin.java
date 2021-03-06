package hello;

import hello.model.User;
import hello.service.Login;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
class CheckLogin implements HandlerInterceptor {

    SqlSessionFactory sqlSessionFactory;
    @Autowired
    Login login;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    Logger logger = LoggerFactory.getLogger(CheckLogin.class);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res,Object handler) throws IOException {
        // 根据token去qq开发平台拿openid
        // 1.如果没有token，则重定向到登录页
        // 2.如果token过期，也重定向到登录页
        Cookie[] rawCookies = req.getCookies();
        if(null == rawCookies) {
            res.sendRedirect("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101833914&redirect_uri=https://jiladahe1997.cn/qqlogin/callback");
            return false;
        }
        Cookie tokenCookie=login.getTokenFromCookie(req);
        if( null == tokenCookie) {
            logger.info("没有cookie，跳转登录页");
            res.sendRedirect("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101833914&redirect_uri=https://jiladahe1997.cn/qqlogin/callback");
            return false;
        }
        // 根据token换取openid
        String openId;
        try {
            openId = this.login.getOpenid(tokenCookie.getValue());
        } catch (Exception e) {
            res.getWriter().print(e.getMessage());
            return false;
        }
        //根据openid去数据库查是否有对应用户
        SqlSession session = this.sqlSessionFactory.openSession();
        User user = session.selectOne("org.mybatis.example.sentenceMapper.selectUserByOpenid", openId);
        if(null == user) {
            res.getWriter().print("该用户不存在");
            return false;
        }
        //正常登录
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
package hello.controller.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.dao.userDao;
import hello.model.Login.UserInfo;
import hello.model.Login.UserInfoVo;
import hello.model.User;
import hello.service.Login;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@RestController
public class GetUserInfo {
    @Autowired
    Login login;


    @RequestMapping("/getUserInfo")
    UserInfoVo GetUserInfo(@CookieValue("_j_token") String token) throws IOException, URISyntaxException {
        // 通过token获取openid
        String openid;
        openid = login.getOpenid(token);
        // 获取用户信息
        URIBuilder uriBuilder = new URIBuilder("https://graph.qq.com/user/get_user_info");
        uriBuilder.setParameters(Arrays.asList(
                new BasicNameValuePair("access_token",token),
                new BasicNameValuePair("openid", openid),
                new BasicNameValuePair("oauth_consumer_key", "101833914")
        ));
        UserInfo userInfo = new ObjectMapper().readerFor(UserInfo.class).readValue(Request.Get(uriBuilder.build()).execute().returnContent().toString());
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setNickname(userInfo.getNickname());
        userInfoVo.setFigureurl(userInfo.getFigureurl_qq_2());
        return userInfoVo;
    }
}

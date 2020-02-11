package hello.controller.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QQLoginController {
    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @RequestMapping("/qqlogin/callback")
    public void loginCallBack(@RequestParam String code, HttpServletResponse httpServletResponse) throws Exception {
        String openid = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = MessageFormat.format("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101833914&client_secret=ca7ba4c1cdac886216ce44a07aa50b56&code={0}&redirect_uri=https://jiladahe1997.cn/",code);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            System.out.println(httpResponse.getStatusLine());
            HttpEntity httpEntity = httpResponse.getEntity();
            String res = EntityUtils.toString(httpEntity);
            Map parsedRes = parseCallBackString(res);
            String token = (String) parsedRes.get("access_token");
            // 再发一次请求换取openId
        try {
            openid = getOpenid(token);
            // 去数据库核对是否有此openid，没有的话则加上
            SqlSession session = this.sqlSessionFactory.openSession();
            User user = session.selectOne("org.mybatis.example.sentenceMapper.selectUserByOpenid", openid);
            if(null == user) {
                session.insert("org.mybatis.example.sentenceMapper.regisUserByOpenid",openid);
            }
            session.commit();
        } catch (Exception e) {
            throw e;
        }
        finally {
            httpClient.close();
        }
        // 重定向到callback路径，并种cookie
        Cookie cookie = new Cookie("_j_token",token);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        httpServletResponse.addCookie(cookie);
        httpServletResponse.sendRedirect("/");
    }

    private String getOpenid(String token) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String openIdUrl = MessageFormat.format("https://graph.qq.com/oauth2.0/me?access_token={0}", token);
        HttpGet httpGet1 = new HttpGet(openIdUrl);
        CloseableHttpResponse httpRespose1 = httpClient.execute(httpGet1);
        HttpEntity httpEntity1 = httpRespose1.getEntity();
        String parsedRes2 = EntityUtils.toString(httpEntity1);
        try {
            // 解析openid
            ObjectMapper mapper = new ObjectMapper();
            JsonNode res = mapper.readTree(parsedRes2.substring(9,parsedRes2.length()-2));
            return res.get("openid").asText();
        } catch (JsonProcessingException e) {
            // 解析返回的openid失败，直接返回qq返回的res
            throw new Exception(parsedRes2);
        }
    }

    private Map parseCallBackString(String res) {
        Map map = new HashMap();
        String[] splitedRes = res.split("&");
        for (String str:splitedRes) {
            map.put(str.split("=")[0],str.split("=")[1]);
        }
        return map;
    }
}

package hello.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QQLoginController {
    @RequestMapping("/qqlogin/callback")
    public String loginCallBack(@RequestParam String code) throws Exception {
        String openid = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = MessageFormat.format("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101833914&client_secret=ca7ba4c1cdac886216ce44a07aa50b56&code={0}&redirect_uri=https://jiladahe1997.cn/",code);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        try {
            System.out.println(httpResponse.getStatusLine());
            HttpEntity httpEntity = httpResponse.getEntity();
            String res = EntityUtils.toString(httpEntity);
            Map parsedRes = parseCallBackString(res);
            // 再发一次请求换取openId
            openid = getOpenid((String) parsedRes.get("access_token"));
            // 去数据库核对是否有此openid，没有的话则加上


            System.out.println(openid);
        } finally {
            httpClient.close();
        }
//        return  "redirect:files/{path}";
        return openid;
    }

    private String getOpenid(String token) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String openIdUrl = MessageFormat.format("https://graph.qq.com/oauth2.0/me?access_token={0}", token);
        HttpGet httpGet1 = new HttpGet(openIdUrl);
        CloseableHttpResponse httpRespose1 = httpClient.execute(httpGet1);
        HttpEntity httpEntity1 = httpRespose1.getEntity();
        ObjectMapper mapper = new ObjectMapper();
        String parsedRes2 = EntityUtils.toString(httpEntity1);
        JsonNode res1 = mapper.readTree(parsedRes2.substring(9,parsedRes2.length()-2));
        return res1.get("openid").asText();
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

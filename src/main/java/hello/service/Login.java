package hello.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;

@Service
public class Login {
    public String getOpenid(String token) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String openIdUrl = MessageFormat.format("https://graph.qq.com/oauth2.0/me?access_token={0}", token);
        HttpGet httpGet = new HttpGet(openIdUrl);
        CloseableHttpResponse httpRespose = httpClient.execute(httpGet);
        HttpEntity httpEntity1 = httpRespose.getEntity();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String parsedRes2 = EntityUtils.toString(httpEntity1);
            JsonNode res = mapper.readTree(parsedRes2.substring(9,parsedRes2.length()-2));
            return res.get("openid").asText();
        } catch (JsonProcessingException e) {
            return EntityUtils.toString(httpEntity1);
        }
    }
}

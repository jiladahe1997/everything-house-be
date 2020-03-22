package hello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TencentCloudKeyControllerTest {

    @Autowired
    private MockMvc mvc;

    private static Cookie cookie;
    @BeforeAll
    static void setCookie(){
        cookie=new Cookie("_j_token","CDF70F2B0B81B85FD6EFE1B757AF3D7D");
    }

    @Test
    void getTencentCloudKey() throws Exception {
        String result=mvc.perform(get("/TencentCloudKey")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON)
                .param("bucket", "sls-cloudfunction-ap-guangzhou-code-1256609098")
                .param("region","ap-guangzhou"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonNode= new ObjectMapper().readTree(result);
        assertEquals(0,jsonNode.get("status").get("code").asInt());
    }
}
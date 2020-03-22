package hello;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.model.Video;

import hello.service.TencentCloudService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import javax.servlet.http.Cookie;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//参考网址https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-mvc-tests

@SpringBootTest
@AutoConfigureMockMvc
class videoControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    TencentCloudService tencentCloudService;

    private static Cookie cookie;
    @BeforeAll
     static void setCookie(){
        cookie=new Cookie("_j_token","CDF70F2B0B81B85FD6EFE1B757AF3D7D");
    }

    @Test
    void getVideos() throws Exception {
        String result=mvc.perform(get("/videos")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("pageSize",String.valueOf(1))
                .param("catagory",String.valueOf(1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonNode= new ObjectMapper().readTree(result);
        assertEquals(0,jsonNode.get("status").get("code").asInt());
    }

    @Test
    void getVideo() throws Exception {
       String result=mvc.perform(get("/videoPlay")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON)
                .param("id",String.valueOf(1)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
       JsonNode jsonNode=new ObjectMapper().readTree(result);
       if(jsonNode.get("data").isNull())
       {
           assertEquals(-1,jsonNode.get("status").get("code").asInt());
       }
       else{
           assertAll("video",
                   ()->assertEquals(0,jsonNode.get("status").get("code").asInt()),
                   ()->assertEquals(1,jsonNode.get("data").get("id").asInt()));
       }

    }


    @Test
    void uploadVideo() throws Exception {
        String bucket="sls-cloudfunction-ap-guangzhou-code-1256609098";
        String region="ap-guangzhou";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat ("yyyy-MM-dd-hh-mm-ss");

        String imgkey=(simpleDateFormat.format(new Date()));
        imgkey=tencentCloudService.putObject(bucket,region,"temp/"+imgkey,"src/test/resources/TencentCloudTestFile.txt");
        imgkey="sls-cloudfunction-ap-guangzhou-code-1256609098.cos.ap-guangzhou.myqcloud.com/"+imgkey;


        String videokey=(simpleDateFormat.format(new Date()));
        videokey=tencentCloudService.putObject(bucket,region,"temp/"+videokey,"src/test/resources/TencentCloudTestFile2.txt");
        videokey="sls-cloudfunction-ap-guangzhou-code-1256609098.cos.ap-guangzhou.myqcloud.com/"+videokey;

        Video video=new Video();
        video.setName("name");
        video.setIntroduction("introduce");
        video.setVideoUrl(videokey);
        video.setImgUrl(imgkey);
        video.setVideoCatagory(0);
        JsonNode jsonParams=new ObjectMapper().valueToTree(video);
        String result=mvc.perform(post("/videoUpload")
                .cookie(cookie)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(jsonParams)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode jsonNode=new ObjectMapper().readTree(result);
        assertEquals(0,jsonNode.get("status").get("code").asInt());
    }
}
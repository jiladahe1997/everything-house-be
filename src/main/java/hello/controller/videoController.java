package hello.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hello.DTO.Result;
import hello.dao.userDao;
import hello.dao.videoDao;
import hello.model.User;
import hello.model.Video;
import hello.service.Login;
import hello.service.TencentCloudService;
import hello.service.myDate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;


@RestController
public class videoController {

    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    Login login;
    @Autowired
    myDate date;
    @Autowired
    TencentCloudService tencentCloudService;

    @GetMapping("/videos")
    @JsonView(Video.simpleView.class)
    public Result getVideos(@RequestParam(name = "page",required = true)int page,
                            @RequestParam(name = "pageSize",required = true)int pageSize,
                            @RequestParam(name = "catagory",required = true)int catagory){
        // 参考https://blog.csdn.net/llkoio/article/details/78939148
        try(SqlSession session=sqlSessionFactory.openSession()) {
            videoDao videoDao=session.getMapper(hello.dao.videoDao.class);

            if(page > 0 && pageSize > 0
                    ||catagory==0||catagory==1||catagory==2||catagory==3||catagory==4||catagory==5) {
                ArrayList<Video> list = videoDao.selectByPage(page, pageSize,catagory);
                session.close();
                Result result = new Result();
                return result.success(list);
            }
            else {
                session.close();
                Result result = new Result();
                return result.fail("非法参数");
            }
        }
    }

    @GetMapping("/videoPlay")
    @JsonView(Video.detailView.class)
    public Result getVideo(@RequestParam(name = "id",required = true)int id){
        try(SqlSession session=sqlSessionFactory.openSession()) {
            videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
            userDao userDao= session.getMapper(hello.dao.userDao.class);
            Video video=videoDao.selectByPrimaryKey(id);
            Result result=new Result();
            if(video!=null) {
                User user = userDao.selectByPrimaryKey(video.getAuthorId());
                session.close();
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode videoNode = objectMapper.valueToTree(video);
                videoNode.put("authorName", user.getNickname());
                videoNode.put("avatar", user.getFigureurl());
                videoNode.remove("id");
                videoNode.remove("authorId");
                videoNode.remove("videoCatagory");
                result.success(videoNode);
                return result;
            }
            return result.fail("未找到该视频");
        }
    }

    @PostMapping("/videoUpload")
    public Result uploadVideo(@RequestBody Video video, HttpServletRequest request) throws IOException{
        Result result = new Result();
        Cookie tokenCookie=login.getTokenFromCookie(request);
        if(tokenCookie == null){
            return result.fail("请先登录");
        }


        URL imgUrl=new URL("https://"+video.getImgUrl());
        String url=URLDecoder.decode(imgUrl.getPath().split("/")[2],"UTF-8");
        String imgSrcKey="temp/"+URLDecoder.decode(imgUrl.getPath().split("/")[2],"UTF-8");
        String imgDesKey="images/"+URLDecoder.decode(imgUrl.getPath().split("/")[2],"UTF-8");
        tencentCloudService.copyObject(tencentCloudService.bucket,imgSrcKey,tencentCloudService.bucket,imgDesKey,tencentCloudService.region);

        URL videoUrl=new URL("https://"+video.getVideoUrl());
        String videoSrcKey="temp/"+URLDecoder.decode(videoUrl.getPath().split("/")[2],"UTF-8");
        String videDesKey="videos/"+URLDecoder.decode(videoUrl.getPath().split("/")[2],"UTF-8");
        tencentCloudService.copyObject(tencentCloudService.bucket,videoSrcKey,tencentCloudService.bucket,videDesKey,tencentCloudService.region);


        try(SqlSession session=sqlSessionFactory.openSession()){
            videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
            String openID;
            try {
                openID=login.getOpenid(tokenCookie.getValue());
            } catch (IOException e) {
                System.out.println("换取openId时出错");
                throw  e;
            }
            userDao userDao=session.getMapper(hello.dao.userDao.class);
            User user=userDao.selectByOpenId(openID);
            int authorId=user.getUid();
            video.setVideoUrl("sls-cloudfunction-ap-guangzhou-code-1256609098.cos.ap-guangzhou.myqcloud.com/"+videDesKey);
            video.setImgUrl("sls-cloudfunction-ap-guangzhou-code-1256609098.cos.ap-guangzhou.myqcloud.com/"+imgDesKey);
            video.setAuthorId(authorId);
            video.setUploadDate(date.getDate());
            videoDao.insert(video);
            session.commit();
            return result.success();
        }
    }
}

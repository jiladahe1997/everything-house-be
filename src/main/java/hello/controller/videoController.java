package hello.controller;

import hello.DTO.Result;
import hello.dao.userDao;
import hello.dao.videoDao;
import hello.model.Common.Status;
import hello.model.Video;
import hello.service.Login;
import hello.service.myDate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;


@RestController
public class videoController {

    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    Login login;
    @Autowired
    myDate date;

    @GetMapping("/api/videos")
    public Result getVideos(@RequestParam(name = "page",required = true)int page,
                                     @RequestParam(name = "pageSize",required = true)int pageSize){
        // 参考https://blog.csdn.net/llkoio/article/details/78939148
        try(SqlSession session=sqlSessionFactory.openSession()) {
            videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
            if(page > 0 && pageSize > 0) {
                ArrayList<Video> list = videoDao.selectByPage(page, pageSize);
                session.close();
                Result result = new Result();
                return result.success(list);
            }
            else {
                ArrayList<Video> list = videoDao.selectByPage(1, 1);
                session.close();
                Result result = new Result();
                return result.success(list);
            }
        }
    }

    @GetMapping("/api/videoPlay")
    public Result getVideo(@RequestParam(name = "id",required = true)int id){
        try(SqlSession session=sqlSessionFactory.openSession()) {
            videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
            Video video=videoDao.selectByPrimaryKey(id);
            session.close();
            Result result = new Result();
            return result.success(video);
        }
    }

    @PostMapping("/api/videoUpload")
    public Result uploadVideo(@RequestBody Video video, HttpServletRequest request) throws IOException{
        Result result = new Result();
        Cookie tokenCookie=login.getTokenFromCookie(request);
        if(tokenCookie == null){
            return result.fail("请先登录");
        }
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
            int authorId=userDao.selectByOpenId(openID).getUid();
            video.setAuthorId(authorId);
            video.setUploadDate(date.getDate());
            videoDao.insert(video);
            session.commit();
            return result.success();
        }
    }
}

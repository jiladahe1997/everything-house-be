package hello.controller;

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
    public ArrayList<Video>getVideos(@RequestParam(name = "page",required = true)int page,
                                     @RequestParam(name = "pageSize",required = true)int pageSize){
        SqlSession session=sqlSessionFactory.openSession();
        videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
        if(page>0&&pageSize>0) {
            ArrayList<Video> list = videoDao.selectByPage(page, pageSize);
            session.close();
            return list;
        }
        else {
            ArrayList<Video> list = videoDao.selectByPage(1, 1);
            session.close();
            return list;
        }
    }

    @GetMapping("/api/videoPlay")
    public Video getVideo(@RequestParam(name = "id",required = true)int id){
        SqlSession session=sqlSessionFactory.openSession();
        videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
        Video video=videoDao.selectByPrimaryKey(id);
        session.close();
        return video;
    }

    @PostMapping("/api/videoUpload")
    public Status uploadVideo(@RequestBody Video video, HttpServletRequest request){
        Status status=new Status();
        Cookie tokenCookie=login.gettokenCookie(request);
        if(tokenCookie==null){
            status.setCode(0);
            status.setMsg(" Authentication failed please log in !");
            return status;
        }
        SqlSession session=sqlSessionFactory.openSession();
        videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
        try {
            String openID=login.getOpenid(tokenCookie.getValue());
            userDao userDao=session.getMapper(hello.dao.userDao.class);
            int authorId=userDao.selectByOpenId(openID).getUid();
            video.setAuthorId(authorId);
            video.setUploadDate(date.getDate());
            videoDao.insert(video);
            session.commit();
            status.setCode(1);
            status.setMsg("success");
        } catch (Exception e) {
            status.setCode(0);
            status.setMsg(e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return status;
    }


}
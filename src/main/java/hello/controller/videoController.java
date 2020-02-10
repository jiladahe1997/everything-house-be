package hello.controller;

import hello.dao.videoDao;
import hello.model.Video;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class videoController {

    SqlSessionFactory sqlSessionFactory;

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }


    @RequestMapping("/api/videoPlay")
    public Video getVideo(@RequestParam(name = "id",required = true)int id){
        SqlSession session = this.sqlSessionFactory.openSession();
        videoDao videoDao=session.getMapper(hello.dao.videoDao.class);
        Video video=videoDao.selectByPrimarykey(1);
//        Video video=session.selectOne("hello.dao.videoDao.selectByPrimaryKey",id);
        int i=0;
        return video;
    }

}

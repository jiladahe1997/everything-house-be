package hello.controller;

import hello.Mybatis;
import hello.model.SentenceVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SentencesController {
    @RequestMapping("/sentences")
    public List<SentenceVo> GetRandomSentence(@RequestParam(value = "id") int id) {
        SqlSessionFactory sqlSessionFactory = Mybatis.getMybatis();
        List<SentenceVo> ret = null;
        try{
            SqlSession session = sqlSessionFactory.openSession();
            ret = session.selectList("org.mybatis.example.sentenceMapper.selectSentence");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}

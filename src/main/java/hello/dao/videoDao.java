package hello.dao;

import hello.model.Video;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;


public interface videoDao {

    int insert(Video video);

    Video selectByPrimaryKey(Integer videoId);

    ArrayList<Video> selectByPage(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

}

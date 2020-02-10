package hello.dao;

import hello.model.Video;

public interface videoDao {

    int insert(Video video);

    Video selectByPrimarykey(Integer videoId);


}

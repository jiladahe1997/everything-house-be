package hello.dao;

import hello.model.User;

public interface userDao {

    User selectByPrimaryKey(Integer uId);

    int insert(User user);

    User selectByOpenId(String openid);
}

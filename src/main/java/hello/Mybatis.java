package hello;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class Mybatis {
    final Logger logger = LoggerFactory.getLogger(Mybatis.class);
    private SqlSessionFactory sqlSessionFactory;
    public Mybatis() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);


        this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }
//    private static SqlSessionFactory mybatisInstants;
//    private Mybatis() {
//        // 配置mybatis
//        try {
//            String resource = "mybatis-config.xml";
//            InputStream inputStream = Resources.getResourceAsStream(resource);;
//            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            mybatisInstants = sqlSessionFactory;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static SqlSessionFactory getMybatis() {
//        if(mybatisInstants == null) {
//            new Mybatis();
//        }
//        return mybatisInstants;
//    }
}

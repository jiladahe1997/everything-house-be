package hello;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;


@Configuration
public class testAppconfig {

    @Primary
    @Bean
    public SqlSessionFactory testSqlSessionFactory() throws IOException {
        String resource = "mybatis-config-test.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        System.out.println("test sqlFactory");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
}

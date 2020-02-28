package hello;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;

@Profile("test")
@Configuration
public class testAppconfig {

    @Bean
    public SqlSessionFactory SqlSessionFactory() throws IOException {
        String resource = "mybatis-config-test.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        System.out.println("test sqlFactory");
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
}

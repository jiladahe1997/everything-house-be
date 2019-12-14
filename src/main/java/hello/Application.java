package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        SpringApplication.run(Application.class, args);
            //配置mysql-jdbc
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    }
}


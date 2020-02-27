package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



@SpringBootApplication
public class Application {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        SpringApplication.run(Application.class, args);
            //配置mysql-jdbc
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    }
}


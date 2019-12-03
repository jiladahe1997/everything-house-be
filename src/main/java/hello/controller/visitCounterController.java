package hello.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@RestController
public class visitCounterController {
    @RequestMapping("/visit_counter")
    public ResultSet counter(HttpServletRequest req){
        // 从数据库取数据
        Connection conn = null;
        ResultSet result = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://cdb-nasxe8jm.cd.tencentcdb.com:10077/everything_house_db?user=root&password=bg250bg%2B");
            Statement statement = conn.createStatement();
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("+8"));
            String sqlString= MessageFormat.format("INSERT INTO visit_count (ip_address, visit_time, visit_url) VALUES (''{0}'', ''{1}'', ''{2}'')", req.getRemoteAddr(), now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), req.getRequestURI());
            int updateState = statement.executeUpdate(sqlString);
            String querySqlString = "SELECT * FROM visit_count";
            result = statement.executeQuery(querySqlString);
            // 之后再往数据库加数据，加快返回速度
        }catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
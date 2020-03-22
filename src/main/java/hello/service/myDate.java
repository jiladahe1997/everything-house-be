package hello.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class myDate {
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
    public String getDate(){
        Date date=new Date();
        return simpleDateFormat.format(date);
    }
}

package hello;

import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.PutObjectResult;
import hello.service.TencentCloudService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TencentCloudServiceTest {

    @Autowired
    TencentCloudService service;

    String bucket="sls-cloudfunction-ap-guangzhou-code-1256609098";
    String region="ap-guangzhou";

    @Test
    void test(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat ("yyyy-MM-dd-hh-mm-ss");
        String key=(simpleDateFormat.format(new Date()));
        key =service.putObject(bucket,region,key,"src/test/resources/TencentCloudTestFile.txt");
        List<COSObjectSummary> result=service.getObjects(bucket,region,"");
        List<String> deleteObjects=new ArrayList();
        for(COSObjectSummary objectSummary:result){
            if(objectSummary.getKey().equals(key)) {
                deleteObjects.add(objectSummary.getKey());
                deleteObjects.add("test/"+objectSummary.getKey());
            }
        }
        service.copyObject(bucket,key,bucket,"test/"+key,region);
        service.deleteMultipleObject(bucket,region,deleteObjects);
    }
}

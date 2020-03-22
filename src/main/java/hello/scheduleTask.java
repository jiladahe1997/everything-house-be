package hello;

import com.qcloud.cos.model.COSObjectSummary;
import hello.service.TencentCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//参考文献 https://www.jianshu.com/p/1defb0f22ed1

@Component
public class scheduleTask {

    @Autowired
    TencentCloudService tencentCloudService;

    @Scheduled(cron="0 0 */1 * * *")
    private void test(){
        List<COSObjectSummary> result=tencentCloudService.getObjects(tencentCloudService.bucket,tencentCloudService.region,"temp/");
        List<String> list=new ArrayList<>();
        for(COSObjectSummary objectSummary:result){
            if(!objectSummary.getKey().equals("temp/")){
                list.add(objectSummary.getKey());
            }
        }
        if(list.size()!=0) {
            tencentCloudService.deleteMultipleObject(tencentCloudService.bucket, tencentCloudService.region, list);
        }
        System.out.println("this is a schedule task : delete temp dircetory!");
    }
}

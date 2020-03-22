package hello.controller;


import hello.DTO.Result;
import hello.model.Common.TencentCloudKey;
import hello.service.TencentCloudService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;



@RestController
public class TencentCloudKeyController {

    @Autowired
    TencentCloudService tencentCloudService;
    //参考网址 https://cloud.tencent.com/document/product/436/14048
    @GetMapping("/TencentCloudKey")
    public Result getTencentCloudKey(@RequestParam(name ="bucket",required = true)String bucket,
                                   @RequestParam(name ="region",required = true)String region){
        try {
            JSONObject data=tencentCloudService.getTempKey(bucket,region);
            TencentCloudKey key=new TencentCloudKey();
            key.setTmpSecretId(data.getJSONObject("credentials").getString("tmpSecretId"));
            key.setTmpSecretKey(data.getJSONObject("credentials").getString("tmpSecretKey"));
            key.setSessionToken(data.getJSONObject("credentials").getString("sessionToken"));
            key.setExpiredTime(data.getLong("expiredTime"));
            key.setStartTime(data.getLong("startTime"));
            Result result=new Result();
            result.success(key);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Result result=new Result();
            result.fail(e.getMessage());
            return result;
        }
    }
}

package hello.controller;

import com.tencent.cloud.CosStsClient;
import hello.DTO.Result;
import hello.model.Common.TencentCloudKey;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.TreeMap;


@RestController
public class TencentCloudKeyController {

    //参考网址 https://cloud.tencent.com/document/product/436/14048
    @GetMapping("/TencentCloudKey")
    public Result getTencentCloudKey(@RequestParam(name ="bucket",required = true)String bucket,
                                   @RequestParam(name ="region",required = true)String region){
        try {
            TreeMap<String, Object> config = new TreeMap<String, Object>();

            config.put("SecretId","AKIDb3HG0Wjpc9QSJuuOFySq1xe7LL6c8wjI");
            config.put("SecretKey","ThgRxEG4dx2tVsypQkob8PQAd6JGyQbX");

            // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
            config.put("durationSeconds",1800);

            config.put("bucket",bucket);
            config.put("region",region);

            //允许路径前缀
            config.put("allowPrefix","*");

            //密钥的权限
            String[] allowActions = new String[]{
                    "name/cos:PutObject"
            };
            config.put("allowActions",allowActions);
            JSONObject data= CosStsClient.getCredential(config);
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

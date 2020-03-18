package hello.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class TencentCloudService {

    public String bucket="sls-cloudfunction-ap-guangzhou-code-1256609098";
    public String region="ap-guangzhou";

    public COSClient intiClient(String cos_region){
        String secretId = "AKIDb3HG0Wjpc9QSJuuOFySq1xe7LL6c8wjI";
        String secretKey = "ThgRxEG4dx2tVsypQkob8PQAd6JGyQbX";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(cos_region);
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    public JSONObject getTempKey(String bucketName,String region) throws IOException {
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        config.put("SecretId","AKIDb3HG0Wjpc9QSJuuOFySq1xe7LL6c8wjI");
        config.put("SecretKey","ThgRxEG4dx2tVsypQkob8PQAd6JGyQbX");

        // 临时密钥有效时长，单位是秒，默认1800秒，最长可设定有效期为7200秒
        config.put("durationSeconds",1800);

        config.put("bucket",bucketName);
        config.put("region",region);

        //允许路径前缀
        config.put("allowPrefix","*");

        //密钥的权限
        String[] allowActions = new String[]{
                "name/cos:PutObject"
        };
        config.put("allowActions",allowActions);
        return CosStsClient.getCredential(config);
    }

    public List<COSObjectSummary> getObjects(String bucketName,String region,String prefix){
        COSClient  cosClient=intiClient(region);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        List<COSObjectSummary> result=new ArrayList<>();
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosClientException e) {
                e.printStackTrace();
                return null;
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            result.addAll(cosObjectSummaries);
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        cosClient.shutdown();
        return result;
    }

    public String putObject(String bucketName,String region,String key, String filePath){
        COSClient cosClient=intiClient(region);
        File file=new File(filePath);
        key=key+file.getName();
        PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, file);
        return key;
    }

    public void copyObject(String srcBucketName,String srcObjectKey,String desBucketName,String desObjectKey,String region){
        COSClient cosClient=intiClient(region);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(srcBucketName, srcObjectKey, desBucketName, desObjectKey);
        CopyObjectResult copyObjectResult = cosClient.copyObject(copyObjectRequest);
    }

    public void deleteMultipleObject(String bucketName,String region,List<String>DeleteObjects ){
        COSClient  cosClient=intiClient(region);

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);

        ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<DeleteObjectsRequest.KeyVersion>();

        for(String key:DeleteObjects){
            keyList.add(new DeleteObjectsRequest.KeyVersion(key));
        }
        deleteObjectsRequest.setKeys(keyList);


        try {
            DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
        } catch (MultiObjectDeleteException mde) { // 如果部分删除成功部分失败, 返回MultiObjectDeleteException
            List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
            List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
        } catch (CosServiceException e) { // 如果是其他错误，例如参数错误， 身份验证不过等会抛出 CosServiceException
            e.printStackTrace();
            throw e;
        } catch (CosClientException e) { // 如果是客户端错误，例如连接不上COS
            e.printStackTrace();
            throw e;
        }
    }
}

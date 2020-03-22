package hello.model.Common;

import lombok.Data;

import java.util.Date;

public class TencentCloudKey {

    private String tmpSecretId;

    private String tmpSecretKey;

    private String sessionToken;

    private Long startTime;

    private Long expiredTime;

    public String getTmpSecretId() {
        return tmpSecretId;
    }

    public String getTmpSecretKey() {
        return tmpSecretKey;
    }

    public String getSessionToken() {
        return sessionToken;
    }


    public Long getStartTime() {
        return startTime;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }


    public void setTmpSecretId(String tmpSecretId) {
        this.tmpSecretId = tmpSecretId;
    }

    public void setTmpSecretKey(String tmpSecretKey) {
        this.tmpSecretKey = tmpSecretKey;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }


    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }
}

package hello.model;

public class User {
    private int uid;
    private String openid;
    private String nickName;
    private String figureUrl;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setNickname(String nickName) {
        this.nickName = nickName;
    }

    public void setFigureurl(String figureUrl) {
        this.figureUrl = figureUrl;
    }

    public int getUid() {
        return uid;
    }

    public String getOpenid() {
        return openid;
    }

    public String getNickname() {
        return nickName;
    }

    public String getFigureurl() {
        return figureUrl;
    }
}

package hello.model;

public class Video {
    private int id;//视频唯一id
    private String name;//视频名称
    private String introduce;//视频简介
    private String videoUrl;//视频链接
    private String imgUrl;//封面图链接
    private int videoCatagory;//视频类别：0（彩虹六号）、1（战地5）、2（CSGO）、3（云顶之弈）、4（生活日常)
    private int authorId;//作者id

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setVideoCatagory(int videoCatagory) {
        this.videoCatagory = videoCatagory;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getVideoCatagory() {
        return videoCatagory;
    }

    public int getAuthorId() {
        return authorId;
    }
}

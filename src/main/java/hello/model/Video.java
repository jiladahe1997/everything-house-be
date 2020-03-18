package hello.model;


import com.fasterxml.jackson.annotation.JsonView;
import hello.DTO.Result;

import java.util.Date;

public class Video {

    public interface simpleView extends Result.resultView{};
    public interface detailView extends simpleView{};

    @JsonView(simpleView.class)
    private int id;//视频唯一id
    @JsonView(simpleView.class)
    private String name;//视频名称
    @JsonView(detailView.class)
    private String introduction;//视频简介
    @JsonView(detailView.class)
    private String videoUrl;//视频链接
    @JsonView(simpleView.class)
    private String imgUrl;//封面图链接
    private int videoCatagory;//视频类别：0（彩虹六号）、1（战地5）、2（CSGO）、3（云顶之弈）、4（生活日常)、5（鬼畜搞笑）

    @JsonView(detailView.class)
    private int authorId;//作者id
    @JsonView(detailView.class)
    private String uploadDate;//上传日期


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
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

    public String getUploadDate() {
        return uploadDate;
    }
}

package com.ristana.e_news.entity.news;

/**
 * Created by hsn on 24/02/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("short")
    @Expose
    private String _short;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("comment")
    @Expose
    private Boolean comment;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShort() {
        return _short;
    }

    public void setShort(String _short) {
        this._short = _short;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Boolean getComment() {
        return comment;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public void setORM(ORMArticle a) {
        String i=a.getNum()+"";
        this.id = Integer.valueOf(i);
        this.title = a.getTitle();
        this.content = a.getContent();
        this.image = a.getImage();
        this.created = a.getCreated();
        this.type = a.getType();
        this._short = a.getShort();
        this.category = a.getCategory();
        this.video = a.getVideo();
        this.comment = a.getComment();
    }

}
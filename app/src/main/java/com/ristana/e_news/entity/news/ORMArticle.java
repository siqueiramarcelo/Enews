package com.ristana.e_news.entity.news;


import com.orm.SugarRecord;

/**
 * Created by hsn on 07/03/2017.
 */

public class ORMArticle extends SugarRecord{
        private Long num;
        private String title;
        private String content;
        private String image;
        private String created;
        private String type;
        private String _short;
        private String category;
        private String video;
        private Boolean comment;

    public ORMArticle() {

    }

    public ORMArticle(Long num, String title, String content, String image, String created, String type, String _short, String category, String video, Boolean comment) {
        this.num = num;
        this.title = title;
        this.content = content;
        this.image = image;
        this.created = created;
        this.type = type;
        this._short = _short;
        this.category = category;
        this.video = video;
        this.comment = comment;
    }

    public ORMArticle(Article a) {
        this.num = Long.valueOf(a.getId());
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

    public Long getNum() {
            return num;
        }

        public void setNum(Long num) {
            this.num = num;
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

}

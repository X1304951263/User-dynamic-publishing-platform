package com.Entity;

public class Comment {
    public int id;
    public String openId_one;
    public String nickName_one;
    public String openId_two;
    public String nickName_two;
    public String content;
    public int articleId;
    public long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenId_one() {
        return openId_one;
    }

    public void setOpenId_one(String openId_one) {
        this.openId_one = openId_one;
    }

    public String getNickName_one() {
        return nickName_one;
    }

    public void setNickName_one(String nickName_one) {
        this.nickName_one = nickName_one;
    }

    public String getOpenId_two() {
        return openId_two;
    }

    public void setOpenId_two(String openId_two) {
        this.openId_two = openId_two;
    }

    public String getNickName_two() {
        return nickName_two;
    }

    public void setNickName_two(String nickName_two) {
        this.nickName_two = nickName_two;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

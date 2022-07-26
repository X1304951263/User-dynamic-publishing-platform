package com.Entity;

public class BlackUser {
    public String openId;
    public String avatar;
    public String nickName;
    public int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

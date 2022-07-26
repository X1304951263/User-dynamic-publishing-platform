package com.DAO;

import com.Entity.AccountUser;
import com.Entity.WechatUser;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @author 13049
 */
@Mapper
public interface WechatUserDao {
    @Results({
            @Result(property = "openId", column = "openId"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nickName"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "campus", column = "campus"),
            @Result(property = "signature", column = "signature"),
            @Result(property = "auth", column = "auth"),
            @Result(property = "state", column = "state")
    })

    //第一次微信授权登录，插入用户数据，并登录
    @Insert("INSERT into user_openid(openId,avatar,nickName,gender,signature) values(#{0},#{1},#{2},#{3},#{4})")
    boolean loginInWechatFirst(String openId, String avatar,String nickName,int gender,String signature);

    //第一次授权登录但是服务出错，插入用户数据失败，后续登录如果没有数据，则重新插入默认数据
    @Insert("INSERT into user_openid(openId,avatar,nickName,signature) values(#{0},#{1},#{2},#{3})")
    boolean retryInsertWechat(String openId,String avatar,String nickName,String signature);

    //微信后续登录
    @Select("SELECT openId,avatar,nickName,gender,campus,signature,auth,state FROM user_openid WHERE openId = #{0} limit 1")
    List<WechatUser> loginInWechat(String openId);

    //修改头像
    @Update("UPDATE user_openid set avatar = #{1}  where openId = #{0}")
    boolean setAvatar(String openId,String avatar);

    //修改头像，获取当前图片位置，删除当前的图片，并更改为最新头像
    @Select("SELECT avatar FROM user_openid WHERE openId = #{0} limit 1")
    List<WechatUser> getAvatarUrl(String openId);

    //修改昵称
    @Update("UPDATE user_openid set nickName = #{1}  where openId = #{0}")
    boolean setNickName(String openId,String nickName);

    //修改性别
    @Update("UPDATE user_openid set gender = #{1}  where openId = #{0}")
    boolean setGender(String openId,int gender);

    //修改校区
    @Update("UPDATE user_openid set campus = #{1}  where openId = #{0}")
    boolean setCampus(String openId,int campus);

    //修改签名
    @Update("UPDATE user_openid set signature = #{1}  where openId = #{0}")
    boolean setSignature(String openId,String signature);

    //获取用户头像和昵称
    @Select("SELECT avatar,nickName FROM user_openid WHERE openId = #{0} limit 1")
    List<WechatUser> getAvatarAndNickName(String openId);

    //添加管理员,更改管理员的角色位
    @Update("UPDATE user_openid set auth = 1 where openId = #{0} and auth = 0")
    boolean setManager(String openId);

    //取消管理员,更改管理员的角色位
    @Update("UPDATE user_openid set auth = 0 where openId = #{0} and auth = 1")
    boolean delManager(String openId);

    //查看用户信息页面
    @Select("SELECT openId,avatar,nickName,gender,campus,signature FROM user_openid WHERE openId = #{0} limit 1")
    List<WechatUser> getUserPage(String openId);

    //封禁用户,更改用户的封禁状态位
    @Update("UPDATE user_openid set state = 1 where openId = #{0} and state = 0")
    boolean lockUser(String openId);

    //解封用户,更改用户的封禁状态位
    @Update("UPDATE user_openid set state = 0 where openId = #{0} and state = 1")
    boolean unlockUser(String openId);
}

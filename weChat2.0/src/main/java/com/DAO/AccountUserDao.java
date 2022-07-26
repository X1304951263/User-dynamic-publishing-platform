package com.DAO;

import com.Entity.AccountUser;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @author 13049
 */
@Mapper
public interface AccountUserDao {
    @Results({
            @Result(property = "openId", column = "openId"),
            @Result(property = "email", column = "email"),
            @Result(property = "word", column = "word"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nickName"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "campus", column = "campus"),
            @Result(property = "signature", column = "signature"),
            @Result(property = "auth", column = "auth"),
            @Result(property = "state", column = "state")
    })

    /**
     * @author 13049
     * 注册账户
     */
    @Insert("INSERT into user_account(openId,email,word,avatar,nickName,signature) values(#{0},#{1},#{2},#{3},#{4},#{5})")
    boolean registerAccount(String openId, String email,String word,String avatar,String nickName,String signature);

    //账户登录
    @Select("SELECT openId,avatar,nickName,gender,campus,signature,auth,state FROM user_account WHERE openId = #{0} and word = #{1} limit 1")
    List<AccountUser> loginInAccount(String openId,String word);

    //邮箱登录
    @Select("SELECT openId,avatar,nickName,gender,campus,signature,auth,state FROM user_account WHERE email = #{0} and word = #{1} limit 1")
    List<AccountUser> loginInEmail(String email,String word);

    //修改头像
    @Update("UPDATE user_account set avatar = #{1}  where openId = #{0}")
    boolean setAvatar(String openId,String avatar);

    //修改头像，获取当前图片位置，删除当前的图片，并更改为最新头像
    @Select("SELECT avatar FROM user_account WHERE openId = #{0} limit 1")
    List<AccountUser> getAvatarUrl(String openId);

    //修改昵称
    @Update("UPDATE user_account set nickName = #{1}  where openId = #{0}")
    boolean setNickName(String openId,String nickName);

    //修改性别
    @Update("UPDATE user_account set gender = #{1}  where openId = #{0}")
    boolean setGender(String openId,int gender);

    //修改校区
    @Update("UPDATE user_account set campus = #{1}  where openId = #{0}")
    boolean setCampus(String openId,int campus);

    //修改签名
    @Update("UPDATE user_account set signature = #{1}  where openId = #{0}")
    boolean setSignature(String openId,String signature);

    //获取用户头像和昵称
    @Select("SELECT avatar,nickName FROM user_account WHERE openId = #{0} limit 1")
    List<AccountUser> getAvatarAndNickName(String openId);

    //添加管理员,更改管理员的角色位
    @Update("UPDATE user_account set auth = 1 where openId = #{0} and auth = 0")
    boolean setManager(String openId);

    //取消管理员,更改管理员的角色位
    @Update("UPDATE user_account set auth = 0 where openId = #{0} and auth = 1")
    boolean delManager(String openId);

    //查看用户信息页面
    @Select("SELECT openId,avatar,nickName,gender,campus,signature FROM user_account WHERE openId = #{0} limit 1")
    List<AccountUser> getUserPage(String openId);

    //封禁用户,更改用户的封禁状态位
    @Update("UPDATE user_account set state = 1 where openId = #{0} and state = 0")
    boolean lockUser(String openId);

    //解封用户,更改用户的封禁状态位
    @Update("UPDATE user_account set state = 0 where openId = #{0} and state = 1")
    boolean unlockUser(String openId);

}

package com.DAO;

import com.Entity.BlackUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlackUserDao {
    @Results({
            @Result(property = "openId", column = "openId"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nickName"),
            @Result(property = "state", column = "state")
    })

    //举报用户，将用户信息插入到黑名单表中以待后续具体操作
    @Insert("INSERT into black_user(openId,avatar,nickName) values(#{0},#{1},#{2})")
    boolean informUser(String openId, String avatar,String nickName);

    //查看举报列表
    @Select("SELECT openId,avatar,nickName FROM black_user WHERE state = 0")
    List<BlackUser> getInformedUser();

    //删除举报信息
    @Delete("DELETE from black_user where openId = #{0} and state = 0")
    boolean delInformedUser(String openId);

    //封禁帐号，更改状态位
    @Update("UPDATE black_user set state = 1 where openId = #{0} and state = 0")
    boolean lockUser(String openId);

    //查看封禁用户列表
    @Select("SELECT openId,avatar,nickName FROM black_user WHERE state = 1")
    List<BlackUser> getLockedUser();

    //解封用户，删除对应数据
    @Delete("DELETE from black_user where openId = #{0} and state = 1")
    boolean unlockUser(String openId);

    //用户更改头像
    @Update("UPDATE black_user set avatar = #{1}  where openId = #{0}")
    boolean updateAvatar(String openId,String avatar);

    //用户更改昵称
    @Update("UPDATE black_user set nickName = #{1}  where openId = #{0}")
    boolean updateNickName(String openId,String nickName);
}

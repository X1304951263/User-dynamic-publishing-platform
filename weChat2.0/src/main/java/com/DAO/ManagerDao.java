package com.DAO;

import com.Entity.Manager;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ManagerDao {
    @Results({
            @Result(property = "openId", column = "openId"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nickName"),
            @Result(property = "state", column = "state")
    })

    //申请成为管理员
    @Insert("INSERT into manager(openId,avatar,nickName) values(#{0},#{1},#{2})")
    boolean insertManger(String openId, String avatar,String nickName);

    //获取申请管理员人员列表
    @Select("SELECT openId,avatar,nickName FROM manager WHERE state = 0")
    List<Manager> getApplyer();

    //获取管理员列表
    @Select("SELECT openId,avatar,nickName FROM manager WHERE state = 1")
    List<Manager> getManager();

    //添加管理员
    @Update("UPDATE manager set state = 1 where openId = #{0} and state = 0")
    boolean setManager(String openId);

    //超级管理员取消普通管理员的身份
    @Delete("DELETE from manager where openId = #{0} and state = 1")
    boolean delManager(String openId);

    //用户更改头像
    @Update("UPDATE manager set avatar = #{1}  where openId = #{0}")
    boolean updateAvatar(String openId,String avatar);

    //用户更改昵称
    @Update("UPDATE manager set nickName = #{1}  where openId = #{0}")
    boolean updateNickName(String openId,String nickName);
}

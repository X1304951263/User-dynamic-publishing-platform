package com.DAO;

import com.Entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "openId_one", column = "openId_one"),
            @Result(property = "nickName_one", column = "nickName_one"),
            @Result(property = "openId_two", column = "openId_two"),
            @Result(property = "nickName_two", column = "nickName_two"),
            @Result(property = "content", column = "content"),
            @Result(property = "articleId", column = "articleId"),
            @Result(property = "timestamp", column = "timestamp"),
    })

    @Select("select id,openId_one,nickName_one,openId_two,nickName_two,content from comment where articleId = #{0} order by id")
    List<Comment> getComments(int articleId);

    //添加评论
    @Insert("INSERT into comment(openId_one,nickName_one,openId_two,nickName_two,content,articleId) values(#{0},#{1},#{2},#{3},#{4},#{5})")
    void insertComment(String openId_one,String nickName_one,String openId_two,String nickName_two,String content,int articleId);

    @Select("select id,openId_one,nickName_one,openId_two,nickName_two,content from comment where articleId = #{0} order by id")
    List<Comment> getComment(String openId);

    //删除评论信息
    @Delete("DELETE from comment where id = #{0} and openId_one = #{1} and articleId = #{2}")
    boolean delComment(int id,String openId,int articleId);

    //用户更改昵称
    @Update("UPDATE comment set nickName_one = #{1}  where openId_one = #{0}")
    boolean updateNickName_one(String openId,String nickName);

    //用户更改昵称
    @Update("UPDATE comment set nickName_two = #{1}  where openId_two = #{0}")
    boolean updateNickName_two(String openId,String nickName);

}

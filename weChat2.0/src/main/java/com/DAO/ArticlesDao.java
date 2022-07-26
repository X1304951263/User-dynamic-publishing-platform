package com.DAO;

import com.Entity.Articles;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ArticlesDao {
    @Results({
            @Result(property = "articleId", column = "articleId"),
            @Result(property = "id", column = "id"),
            @Result(property = "openId", column = "openId"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nickName"),
            @Result(property = "content", column = "content"),
            @Result(property = "imgs", column = "imgs"),
            @Result(property = "timestamp", column = "timestamp"),
            @Result(property = "color", column = "color"),
            @Result(property = "love", column = "love"),
            @Result(property = "comment", column = "comment"),
    })

    //管理员发布动态
    @Insert("INSERT into articles(id,openId,avatar,nickName,content,imgs,timestamp,color) values(#{0},#{1},#{2},#{3},#{4},#{5},#{6},#{7})")
    boolean publishArticle(int id,String openId, String avatar,String nickName,String content,String imgs,long timestamp,int color);

    //根据id获取对应动态信息
    @Select("SELECT articleId,id,openId,avatar,nickName,content,imgs,timestamp,color,love,comment FROM articles where id = #{0}")
    List<Articles> getArticlesById(int id);

    //用户查看第id期动态
    @Select("SELECT articleId,id,openId,avatar,nickName,content,imgs,timestamp,color,love,comment FROM articles order by articleId")
    List<Articles> getArticles(int id);

    //获取某条动态的点赞的数量
    @Select("SELECT love FROM articles where articleId = #{0}")
    Integer getLovesNum(int articleId);

    //修改某条动态的点赞数量
    @Update("UPDATE articles set love = #{1}  where articleId = #{0}")
    boolean setLove(int articleId,int love);

    //获取某条动态的评论的数量
    @Select("SELECT comment FROM articles where articleId = #{0}")
    Integer getCommentsNum(int articleId);

    //修改某条动态的评论数量
    @Update("UPDATE articles set comment = #{1}  where articleId = #{0}")
    boolean setComment(int articleId,int commentNum);

    //用户更改头像
    @Update("UPDATE articles set avatar = #{1}  where openId = #{0}")
    boolean updateAvatar(String openId,String avatar);

    //用户更改昵称
    @Update("UPDATE articles set nickName = #{1}  where openId = #{0}")
    boolean updateNickName(String openId,String nickName);
}

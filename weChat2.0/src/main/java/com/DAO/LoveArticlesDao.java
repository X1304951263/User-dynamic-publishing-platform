package com.DAO;

import com.Entity.LoveArticles;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LoveArticlesDao {
    @Results({
            @Result(property = "articleId", column = "articleId"),
            @Result(property = "openId", column = "openId"),
            @Result(property = "id", column = "id")
    })

    //用户获取第id期d动态点赞列表
    @Select("select articleId from love_article where openId = #{0} and id = #{1}")
    List<LoveArticles> getLoves(String openId, int id);

    //删除点赞信息
    @Delete("DELETE from love_article where articleId = #{0} and openId = #{1}")
    boolean delLove(int articleId,String openId);
    //插入点赞信息
    @Insert("INSERT into love_article(articleId,openId,id) values(#{0},#{1},#{2})")
    boolean insertLove(int articleId,String openId,int id);

    //获取用户点赞某个动态的点赞信息
    @Select("select articleId,openId from love_article where articleId = #{0} and openId = #{1} limit 1")
    List<LoveArticles> getLoveByOpenId(int articleId,String openId);

    @Select("select openId from love_article where articleId = #{0}")
    List<LoveArticles> getArticleLoves(int articleId);
}

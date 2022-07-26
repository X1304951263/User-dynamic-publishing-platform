package com.DAO;

import com.Entity.UserArticleWait;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserWaitArticleDao {
    @Results({
            @Result(property = "articleId", column = "articleId"),
            @Result(property = "openId", column = "openId"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nickName"),
            @Result(property = "content", column = "content"),
            @Result(property = "imgs", column = "imgs"),
            @Result(property = "timestamp", column = "timestamp"),
            @Result(property = "color", column = "color"),
            @Result(property = "state", column = "state")
    })

    //用户发表动态
    @Insert("INSERT into user_article_wait(openId,avatar,nickName,content,imgs,timestamp,color) values(#{0},#{1},#{2},#{3},#{4},#{5},#{6})")
    boolean submitArticle(String openId, String avatar,String nickName,String content,String imgs,long timestamp,int color);

    //管理员获取所有动态
    @Select("SELECT * FROM user_article_wait where state = #{0}")
    List<UserArticleWait> getAllWaitArticle(int state);

    //管理员选择动态
    @Update("UPDATE user_article_wait set state = #{1}  where articleId = #{0}")
    boolean chooseArticle(int articleId,int state);

    //管理员标红动态
    @Update("UPDATE user_article_wait set color = #{1}  where articleId = #{0}")
    boolean setRed(int articleId,int state);

    //管理员屏蔽已经选择的动态
    @Update("UPDATE user_article_wait set imgs = #{1},content = #{2}  where articleId = #{0} and state = 1")
    boolean hideArticle(int articleId,String imgs,String content);

    //获取动态的图片路径，方便后面的操作删除图片文件
    @Select("SELECT imgs FROM user_article_wait where articleId = #{0} limit 1")
    List<UserArticleWait> getImgs(int articleId);


    //管理员删除动态
    @Delete("DELETE from user_article_wait where articleId = #{0}")
    boolean delArticleById(int articleId);

    //用户更改头像
    @Update("UPDATE user_article_wait set avatar = #{1}  where openId = #{0}")
    boolean updateAvatar(String openId,String avatar);

    //用户更改昵称
    @Update("UPDATE user_article_wait set nickName = #{1}  where openId = #{0}")
    boolean updateNickName(String openId,String nickName);

}

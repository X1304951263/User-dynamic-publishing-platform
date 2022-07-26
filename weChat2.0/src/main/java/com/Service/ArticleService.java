package com.Service;

import com.VO.Result;

public interface ArticleService {
    //首页动态信息
    Result getArticles(String openId);

    //获取对应期动态信息
    Result getArticlesBySurfaceId(String openId,int id);

    //获取所有封面
    Result getAllSurface();
    //用户点赞某个动态
    Result loveArticle(String openId,int articleId,int id);
    //获取某条动态的所有评论信息
    Result getComments(int articleId);
    //用户给某条动态添加评论
    Result insertComment(String openId_one,String nickName_one,String openId_two,String nickName_two
            ,String content,int articleId);
    //删除评论
    Result delComment(int id,String openId,int articleId);
}

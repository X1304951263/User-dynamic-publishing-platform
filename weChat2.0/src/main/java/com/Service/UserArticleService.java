package com.Service;

import com.VO.Result;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface UserArticleService {
    //用户发表动态
    Result submitArticle(String openId,int signin,String content, List<String> imgs,int color);

    //管理员获取所有未审核动态
    Result getAllWaitArticle();

    //管理员获取所有已审核动态
    Result getArticles();

    //管理员选择动态
    Result chooseArticle(int articleId);

    //管理员标红动态
    Result setRed(int articleId);

    //管理员屏蔽动态
    Result hideArticle(int articleId);

    //管理员删除动态
    Result delArticleById(int articleId);

    //管理员发布动态，同时添加本期动态的封面
    Result publishArticle(MultipartFile file, String content);
}

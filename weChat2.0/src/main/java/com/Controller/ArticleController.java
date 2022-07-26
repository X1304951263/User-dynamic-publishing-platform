package com.Controller;

import com.Service.ArticleService;
import com.Utils.TokenUtil;
import com.VO.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/article/get")
    public Result getArticls(HttpServletRequest request){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        return articleService.getArticles(openId);
    }

    @PostMapping(value = "/article/getById")
    public Result getArticlesBySurfaceId(HttpServletRequest request,@RequestBody Map<String,Integer> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        return articleService.getArticlesBySurfaceId(openId,map.get("id"));
    }

    @PostMapping(value = "/article/love")
    public Result loveArticle(HttpServletRequest request, @RequestBody Map<String,Integer> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        int articleId = map.get("articleId");
        int id = map.get("id");
        return articleService.loveArticle(openId,articleId,id);
    }

    @PostMapping(value = "/article/comment/get")
    public Result getComments(HttpServletRequest request, @RequestBody Map<String,Integer> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        int articleId = map.get("articleId");
        return articleService.getComments(articleId);
    }

    @PostMapping(value = "/article/comment/insert")
    public Result insertComment(HttpServletRequest request, @RequestBody Map<String,Object> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        String nickName1 = String.valueOf(map.get("nickName_one")) ;
        String openId_two = String.valueOf(map.get("openId_two"));
        String nickName2 = String.valueOf(map.get("nickName_two"));
        String content = String.valueOf(map.get("content"));
        int articleId = (int) map.get("articleId");
        return articleService.insertComment(openId,nickName1,openId_two,nickName2,content,articleId);
    }

    @PostMapping(value = "/article/comment/del")
    public Result delComment(HttpServletRequest request, @RequestBody Map<String,Object> map){
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);
        int id = (int)map.get("id");
        int articleId = (int) map.get("articleId");
        return articleService.delComment(id,openId,articleId);
    }

    @PostMapping(value = "/article/get/allSurface")
    public Result getAllSurface(){
        return articleService.getAllSurface();
    }
}

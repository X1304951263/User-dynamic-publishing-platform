package com.Controller;

import com.Service.UserArticleService;
import com.Utils.Constant;
import com.Utils.TokenUtil;
import com.VO.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class UserArticleController {
    @Autowired
    private UserArticleService userArticleService;

    @PostMapping(value = "/user/submit/article")
    public Result submitArticle(HttpServletRequest request,@RequestBody Map<String,Object> map) throws IOException {
        String token = request.getHeader("token");
        String openId = TokenUtil.getOpenId(token);;
        int singin = TokenUtil.getSignin(token);
        List<String> imgs = (List) map.get("imgBase64");
        String content = (String) map.get("content");
        int color = (int) map.get("color");
        return userArticleService.submitArticle(openId,singin,content,imgs,color);
    }

    @PostMapping(value = "/manager/get/articles")
    public Result getAllWaitArticle(HttpServletRequest request){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);;
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.getAllWaitArticle();
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }

    @PostMapping(value = "/manager/get/articled")
    public Result getArticles(HttpServletRequest request){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.getArticles();
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }

    @PostMapping(value = "/manager/choose/article")
    public Result publishArticle(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        int id = 0;
        try {
            id = Integer.parseInt(map.get("id"));
        }catch (Exception e){
            Result res = new Result();
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.chooseArticle(id);
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }

    @PostMapping(value = "/manager/del/article")
    public Result delArticle(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        int id = 0;
        try {
            id = Integer.parseInt(map.get("id"));
        }catch (Exception e){
            Result res = new Result();
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.delArticleById(id);
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }

    @PostMapping(value = "/manager/hide/article")
    public Result hideArticle(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        int id = 0;
        try {
            id = Integer.parseInt(map.get("id"));
        }catch (Exception e){
            Result res = new Result();
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.hideArticle(id);
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }

    @PostMapping(value = "/manager/setRed/article")
    public Result redArticle(HttpServletRequest request,@RequestBody Map<String,String> map){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        int id = 0;
        try {
            id = Integer.parseInt(map.get("id"));
        }catch (Exception e){
            Result res = new Result();
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.setRed(id);
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }

    @PostMapping(value = "/manager/push/articles")
    public Result pubArticle(HttpServletRequest request, MultipartFile img, String content){
        String token = request.getHeader("token");
        int auth = TokenUtil.getAuth(token);
        if(auth == Constant.MANAGER_AUTH || auth == Constant.ADMIN_AUTH){
            return userArticleService.publishArticle(img,content);
        }else {
            Result res = new Result();
            res.setCode(Constant.NO_PERMISSION);
            res.setMessage("您没有权限！");
            return res;
        }
    }
}

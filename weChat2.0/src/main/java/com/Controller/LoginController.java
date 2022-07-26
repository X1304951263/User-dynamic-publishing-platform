package com.Controller;

import com.Service.LoginService;
import com.VO.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Map;


/**
 * @author 13049
 */
@Controller
@CrossOrigin
@ResponseBody
public class LoginController {

    @Autowired
    private LoginService loginService;

    //注册账户，发送验证码
    @PostMapping(value = "/login/virifyCode")
    public Result sendVirifyCode(@RequestBody Map<String,String> map){
        return loginService.sendVirifyCode(map);
    }

    //注册账户
    @PostMapping(value = "/login/register")
    public Result registerAccount(@RequestBody Map<String,String> map){
        return loginService.registerAccount(map);
    }

    //账号或者邮箱登录
    @PostMapping(value = "/login/account")
    public Result loginInAccount(@RequestBody Map<String,String> map){
        return loginService.loginInAccount(map);
    }

    //第一次微信授权登录
    @PostMapping(value = "/login/firstWechat")
    public Result loginInWechatFirst(@RequestBody Map<String,String> map){
        return loginService.loginInWechatFirst(map);
    }

    //后续微信授权登录
    @PostMapping(value = "/login/wechat")
    public Result loginInWechat(@RequestBody Map<String,String> map){
        return loginService.loginInWechat(map);
    }




    //上传视频
    @PostMapping(value = "uploadVideo")
    public Result uploadVideo(@RequestParam(value="video",required = true) MultipartFile file)
            throws Exception {
        String originName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
       // UpVideoToALiYun.UploadStream("抖音",originName,inputStream);
        return new Result();
    }

}

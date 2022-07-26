package com.Service;

import com.VO.Result;

import java.util.Map;

/**
 * @author 13049
 * 登录服务
 */
public interface LoginService {
    //注册发送验证码
    Result sendVirifyCode(Map<String,String> map);
    //用户注册
    Result registerAccount(Map<String,String> map);
    //账户登录
    Result loginInAccount(Map<String,String> map);
    /**
     * 用户第一次登录此程序，获取用户的openid及一些其他信息，存入用户表，相当于注册
     * @param map 前端传来的用户的信息
     * @return 返回授权结果，成功或者失败
     * */
    Result loginInWechatFirst(Map<String,String> map);

    /**
     * 用户已授权，登录
     *  code 前端传来的微信开放能力code，用来换取用户openid
     * @return 返回登录结果，成功或者失败
     * */
    Result loginInWechat(Map<String,String> map);


}

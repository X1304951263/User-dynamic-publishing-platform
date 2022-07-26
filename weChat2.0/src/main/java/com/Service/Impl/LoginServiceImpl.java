package com.Service.Impl;

import com.DAO.AccountUserDao;
import com.DAO.WechatUserDao;
import com.Entity.AccountUser;
import com.Entity.WechatUser;
import com.Service.LoginService;
import com.Utils.*;
import com.VO.Result;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 13049
 */
@Service
@Log4j2
public class LoginServiceImpl implements LoginService {
    public final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private AccountUserDao accountUserDao;

    @Autowired
    private WechatUserDao wechatUserDao;

    @Qualifier("redisTemplateString")
    @Autowired
    private RedisTemplate<String, String> redisTemplateString;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public Result sendVirifyCode(Map<String,String> map) {
        String email = map.get("email");
        Result res = new Result();
        if(!ParamCheckUtil.isEmail(email)){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        //生成验证码
        String code = SendVirifyCode.getVerifyCode();
        try {
            if(redisTemplateString.hasKey(Constant.PRE_REGISTER_VIRIFYCODE_COUNT_KEY + email)){
                int count = Integer.parseInt(
                        redisTemplateString.opsForValue().get(Constant.PRE_REGISTER_VIRIFYCODE_COUNT_KEY + email));
                if(count == Constant.VIRIFYCODE_COUNT){
                    res.setCode(Constant.VIRIFYCODE_OVERCOUNT);
                    res.setMessage("邮箱单日获取验证码次数已达上限！");
                    return res;
                }
                mailSender.send(SendVirifyCode.sendMes(email,code));
                //将邮箱-验证码k-v 放入redis，失效时间默认5分钟
                redisTemplateString.opsForValue()
                        .set(Constant.PRE_REGISTER_VIRIFYCODE_NUMBER_KEY + email,code,Constant.VIRIFYCODE_EXPIRE_TIME, TimeUnit.SECONDS);
                //将当前邮箱发送验证码次数+1
                redisTemplateString.opsForValue()
                        .increment(Constant.PRE_REGISTER_VIRIFYCODE_COUNT_KEY + email,1);
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("发送验证码成功！");
            }else{
                mailSender.send(SendVirifyCode.sendMes(email,code));
                //邮箱当天第一次发送验证码，设置次数为1存入redis，过期时间默认为24小时
                redisTemplateString.opsForValue()
                        .set(Constant.PRE_REGISTER_VIRIFYCODE_COUNT_KEY + email, String.valueOf(1),Constant.VIRIFYCODE_COUNT_TIME,TimeUnit.SECONDS);
                redisTemplateString.opsForValue()
                        .set(Constant.PRE_REGISTER_VIRIFYCODE_NUMBER_KEY + email,code,Constant.VIRIFYCODE_EXPIRE_TIME,TimeUnit.SECONDS);
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("发送验证码成功！");
            }
        }catch (Exception e){
            logger.error("邮箱：<" + email + ">,发送验证码失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，发送失败！");
        }
        return res;
    }

    @Override
    public Result registerAccount(Map<String, String> map) {
        Result res = new Result();
        String openId = map.get("openId");
        String email = map.get("email");
        String word = map.get("word");
        String code = map.get("code");
        if(!ParamCheckUtil.isCode(code) || !ParamCheckUtil.isAccount(openId) ||
                !ParamCheckUtil.isEmail(email) || !ParamCheckUtil.isWord(word)){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        try {
            if(redisTemplateString.hasKey(Constant.PRE_REGISTER_VIRIFYCODE_NUMBER_KEY + email)){
                String code1 = redisTemplateString.opsForValue().get(Constant.PRE_REGISTER_VIRIFYCODE_NUMBER_KEY + email);
                if(code.equals(code1)){
                    redisTemplateString.delete(Constant.PRE_REGISTER_VIRIFYCODE_NUMBER_KEY + email);
                    boolean p = accountUserDao.registerAccount(openId,email,word,Constant.DEFAULT_AVATAR_URL,
                            Constant.DEFAULT_NICKNAME,Constant.DEFAULT_SIGNATURE);
                    if(p){
                        String token = TokenUtil.sign(openId,Constant.USER_AUTH,Constant.LOGIN_IN_ACCOUNT);
                        AccountUser user = new AccountUser();
                        user.setAvatar(Constant.DEFAULT_AVATAR_URL);
                        user.setNickName(Constant.DEFAULT_NICKNAME);
                        user.setGender(Constant.DEFAULT_GENDER);
                        user.setCampus(Constant.DEFAULT_CAMPUS);
                        user.setSignature(Constant.DEFAULT_SIGNATURE);
                        Map<String,Object> resMap = new HashMap<>();
                        resMap.put("token",token);
                        resMap.put("userInfo",user);
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("注册成功！");
                        res.setData(resMap);
                        return res;
                    }else{
                        res.setCode(Constant.MYSQL_INFO_REPEAT);
                        res.setMessage("该邮箱已注册！");
                        return res;
                    }
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("验证码错误！");
        }catch (Exception e){
            logger.error("Email:<" + email + ">，注册失败！");
            res.setCode(Constant.MYSQL_INFO_REPEAT);
            res.setMessage("该邮箱已注册！");
            res.setData("");
        }
        return res;
    }

    @Override
    public Result loginInAccount(Map<String, String> map) {
        Result res = new Result();
        String word = map.get("word");
        if(!ParamCheckUtil.isWord(word)){
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("密码错误！");
            return res;
        }
        String openId = map.get("openId");
        if(openId == null || openId.equals("")){
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("账号错误！");
            return res;
        }
        List<AccountUser> list = new ArrayList<>();
        try {
            if(ParamCheckUtil.isEmail(openId)){
                list = accountUserDao.loginInEmail(openId,word);
            } else {
                list = accountUserDao.loginInAccount(openId,word);
            }
            if(list.isEmpty()){
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("账号或密码错误！");
                return res;
            }
            AccountUser user = list.get(0);
            if(user.getState() == Constant.USER_IN_BLACK){
                res.setCode(Constant.USER_INT_BLACKLIST);
                res.setMessage("账号已被封禁！");
                return res;
            }
            String token = TokenUtil.sign(user.getOpenId(),user.getAuth(),Constant.LOGIN_IN_ACCOUNT);
            Map<String,Object> resMap = new HashMap<>();
            resMap.put("token",token);
            resMap.put("userInfo",user);
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("登录成功！");
            res.setData(resMap);
        }catch (Exception e){
            logger.error("Account:<" + openId + ">，登录失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，登录失败！");
        }
        return res;
    }

    @Override
    public Result loginInWechatFirst(Map<String, String> map) {
        Result res = new Result();
        String code = map.get("code");
        String openId = GetOpenId.getOpenId(code);
        if(GetOpenId.GETCODE_ERROR.equals(openId) || "".equals(openId)){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("code码错误！");
            return res;
        }
        String avatar = map.get("avatar");
        String nickName = map.get("nickName");
        int gender = Integer.parseInt(map.get("gender"));
        try {
            List<WechatUser> list = wechatUserDao.loginInWechat(openId);
            if(!list.isEmpty()){
                WechatUser user1 = list.get(0);
                if(user1.getState() == Constant.USER_IN_BLACK){
                    res.setCode(Constant.USER_INT_BLACKLIST);
                    res.setMessage("账号已被封禁！");
                    return res;
                }
                String token = TokenUtil.sign(openId,user1.getAuth(),Constant.LOGIN_IN_WECHAT);
                Map<String,Object> resMap = new HashMap<>();
                resMap.put("token",token);
                resMap.put("userInfo",user1);
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("登录成功！");
                res.setData(resMap);
                return res;
            }
            boolean p = wechatUserDao.loginInWechatFirst(openId,avatar,nickName,gender,Constant.DEFAULT_SIGNATURE);
            if(p){
                WechatUser user = new WechatUser();
                user.setNickName(nickName);
                user.setSignature(Constant.DEFAULT_SIGNATURE);
                user.setGender(gender);
                user.setCampus(Constant.DEFAULT_CAMPUS);
                user.setAvatar(avatar);
                String token = TokenUtil.sign(openId,Constant.USER_AUTH,Constant.LOGIN_IN_WECHAT);
                Map<String,Object> resMap = new HashMap<>();
                resMap.put("token",token);
                resMap.put("userInfo",user);
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("登录成功！");
                res.setData(resMap);
                return res;
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("授权登录失败！");
        }catch (Exception e){
            logger.error("OpenId:<" + openId + ">，微信授权登录失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，登录失败！");
        }
        return res;
    }

    @Override
    public Result loginInWechat(Map<String, String> map) {
        Result res = new Result();
        String code = map.get("code");
        String openId = GetOpenId.getOpenId(code);
        if(GetOpenId.GETCODE_ERROR.equals(openId)){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("code码错误！");
            return res;
        }
        try {
            List<WechatUser> user = wechatUserDao.loginInWechat(openId);
            //用户已授权但数据库没有该openid信息，则重新插入该openid数据，给一个默认信息，并返回登录成功信息
            if(user.isEmpty()){
                boolean p = wechatUserDao.retryInsertWechat(openId,Constant.DEFAULT_AVATAR_URL,
                        Constant.DEFAULT_NICKNAME,Constant.DEFAULT_SIGNATURE);
                if(p){
                    WechatUser resUser = new WechatUser();
                    resUser.setAvatar(Constant.DEFAULT_AVATAR_URL);
                    resUser.setNickName(Constant.DEFAULT_NICKNAME);
                    resUser.setGender(Constant.DEFAULT_GENDER);
                    resUser.setCampus(Constant.DEFAULT_CAMPUS);
                    resUser.setSignature(Constant.DEFAULT_SIGNATURE);
                    resUser.setAuth(Constant.USER_AUTH);
                    String token = TokenUtil.sign(openId,Constant.USER_AUTH,Constant.LOGIN_IN_WECHAT);
                    Map<String,Object> resMap = new HashMap<>();
                    resMap.put("token",token);
                    resMap.put("userInfo",resUser);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("登录成功！");
                    res.setData(resMap);
                    return res;
                }
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("登录失败！");
                return res;
            }
            WechatUser user1 = user.get(0);
            if(user1.getState() == Constant.USER_IN_BLACK){
                res.setCode(Constant.USER_INT_BLACKLIST);
                res.setMessage("账号已被封禁！");
                return res;
            }
            String token = TokenUtil.sign(openId,user1.getAuth(),Constant.LOGIN_IN_WECHAT);
            Map<String,Object> resMap = new HashMap<>();
            resMap.put("token",token);
            resMap.put("userInfo",user1);
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("登录成功！");
            res.setData(resMap);
        }catch (Exception e){
            logger.error("OpenId:<" + openId + ">，微信登录失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，登录失败！");
        }
        return res;
    }


}

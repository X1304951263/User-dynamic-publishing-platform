package com.Service;

import com.VO.Result;
import org.springframework.web.multipart.MultipartFile;

/*
* 用户信息增删改查的服务
* */
public interface UserService {
    //修改头像
    Result setAvatar(String openId, MultipartFile file, int signin);

    //修改昵称
    Result setNickName(String openId, String nickName,int signin);

    //修改性别
    Result setGender(String openId,int gender,int signin);

    //修改校区
    Result setCampus(String openId,int campus,int signin);

    //修改签名
    Result setSignature(String openId,String signature,int signin);

    //申请管理员，将用户信息插入待选管理员列表
    Result insertManger(String openId);

    //超级管理员获取申请成为管理员的人员列表
    Result getApplyer();

    //超级管理员获取管理员列表
    Result getManager();

    //添加管理员，更改权限位
    Result setManager(String openId);

    //取消管理员，更改权限位，并将管理员表的数据删除
    Result delManager(String openId);

    //查看用户信息页面
    Result getUserPage(String openId);

    //举报用户
    Result informUser(String openId,String content,String imgs);

    //查看被举报用户列表
    Result getInformedUser();

    //查看举报原因
    Result getInformReason(String openId);

    //删除举报信息
    Result delInformedUser(String openId);

    //封禁用户
    Result lockUser(String openId);

    //获取封禁用户列表
    Result getLockedUser();

    //解封用户
    Result unlockUser(String openId) ;
}

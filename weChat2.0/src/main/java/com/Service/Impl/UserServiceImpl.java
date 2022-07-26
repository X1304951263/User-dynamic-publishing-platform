package com.Service.Impl;

import com.DAO.*;
import com.Entity.*;
import com.Service.UserService;
import com.Utils.BASE64DecodedMultipartFile;
import com.Utils.Constant;
import com.VO.Result;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    public final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private AccountUserDao accountUserDao;

    @Autowired
    private WechatUserDao wechatUserDao;

    @Autowired
    private ManagerDao managerDao;

    @Autowired
    private BlackUserDao blackUserDao;

    @Autowired
    private BlackReasonDao blackReasonDao;

    @Autowired
    private ArticlesDao articlesDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserWaitArticleDao userWaitArticleDao;

    @Autowired
    private Executor executor;

    @Override
    public Result setAvatar(String openId, MultipartFile file, int signin) {
        Result res = new Result();
        if(file == null){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        if(signin != Constant.LOGIN_IN_WECHAT && signin !=Constant.LOGIN_IN_ACCOUNT){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("登录渠道错误！");
            return res;
        }
        String avatar = "";
        try {
            /*String allName = file.getOriginalFilename();
            String name = System.currentTimeMillis() + openId + allName.substring(allName.lastIndexOf("."));
            file.transferTo(new File(Constant.AVATAR_URL + name));
            avatar = Constant.AVATAR_SERVER_URL + name;*/
            if(signin == Constant.LOGIN_IN_ACCOUNT){
                List<AccountUser> list = accountUserDao.getAvatarUrl(openId);
                if(list.isEmpty()){
                    res.setCode(Constant.NO_DATA_IN_MYSQL);
                    res.setMessage("数据库没有该用户信息");
                    return res;
                }
                String delAvatarUrl = list.get(0).getAvatar();
                String allName = file.getOriginalFilename();
                String name = System.currentTimeMillis() + openId + allName.substring(allName.lastIndexOf("."));
                avatar = Constant.AVATAR_SERVER_URL + name;
                boolean p = accountUserDao.setAvatar(openId, avatar);
                if(p){
                    file.transferTo(new File(Constant.AVATAR_URL + name));
                    String avatarUrl = avatar;
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                articlesDao.updateAvatar(openId,avatarUrl);
                                userWaitArticleDao.updateAvatar(openId,avatarUrl);
                                blackUserDao.updateAvatar(openId,avatarUrl);
                                managerDao.updateAvatar(openId,avatarUrl);
                                String[] str = delAvatarUrl.split("/");
                                new File(Constant.AVATAR_URL + str[str.length - 1]).delete();
                            }catch(Exception e){
                                logger.info("用户：" + openId + "，更改头像后，异步更改动态表、黑名单表，管理员表的头像失败！");
                            }
                        }
                    });
                    Map<String,Object> resMap = new HashMap<>();
                    resMap.put("avatar",avatar);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    res.setData(resMap);
                    return res;
                }
            }else if(signin == Constant.LOGIN_IN_WECHAT){
                List<WechatUser> list = wechatUserDao.getAvatarUrl(openId);
                if(list.isEmpty()){
                    return null;
                }
                String delAvatarUrl = list.get(0).getAvatar();
                String allName = file.getOriginalFilename();
                String name = System.currentTimeMillis() + openId + allName.substring(allName.lastIndexOf("."));
                avatar = Constant.AVATAR_SERVER_URL + name;
                boolean p = wechatUserDao.setAvatar(openId, avatar);
                if(p){
                    file.transferTo(new File(Constant.AVATAR_URL + name));
                    String avatarUrl = avatar;
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                articlesDao.updateAvatar(openId,avatarUrl);
                                userWaitArticleDao.updateAvatar(openId,avatarUrl);
                                blackUserDao.updateAvatar(openId,avatarUrl);
                                managerDao.updateAvatar(openId,avatarUrl);
                                String[] str = delAvatarUrl.split("/");
                                new File(Constant.AVATAR_URL + str[str.length - 1]).delete();
                            }catch(Exception e){
                                logger.info("用户：" + openId + "，更改头像后，异步更改动态表、黑名单表，管理员表的头像失败！");
                            }
                        }
                    });
                    Map<String,Object> resMap = new HashMap<>();
                    resMap.put("avatar",avatar);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    res.setData(resMap);
                    return res;
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("修改失败！");
        }catch (Exception e){
            logger.error("openId：<" +openId + ": " +  avatar + ">,修改头像失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，修改失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result setNickName(String openId, String nickName,int signin) {
        Result res = new Result();
        if(nickName == null || nickName.equals("") || nickName.length() > Constant.NICKNAME_MAX_LENGTH){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        if(signin != Constant.LOGIN_IN_WECHAT && signin !=Constant.LOGIN_IN_ACCOUNT){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("登录渠道错误！");
            return res;
        }
        try {
            if(signin == Constant.LOGIN_IN_ACCOUNT){
                boolean p = accountUserDao.setNickName(openId, nickName);
                if(p){
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                articlesDao.updateNickName(openId,nickName);
                                userWaitArticleDao.updateNickName(openId, nickName);
                                blackUserDao.updateNickName(openId, nickName);
                                managerDao.updateNickName(openId, nickName);
                                commentDao.updateNickName_one(openId, nickName);
                                commentDao.updateNickName_two(openId, nickName);
                            }catch(Exception e){
                                logger.info("用户：" + openId + "，更改头像后，异步更改动态表、黑名单表，管理员表，评论表的昵称失败！");
                            }
                        }
                    });
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }else if(signin == Constant.LOGIN_IN_WECHAT){
                boolean p = wechatUserDao.setNickName(openId, nickName);
                if(p){
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                articlesDao.updateNickName(openId,nickName);
                                userWaitArticleDao.updateNickName(openId, nickName);
                                blackUserDao.updateNickName(openId, nickName);
                                managerDao.updateNickName(openId, nickName);
                                commentDao.updateNickName_one(openId, nickName);
                                commentDao.updateNickName_two(openId, nickName);
                            }catch(Exception e){
                                logger.info("用户：" + openId + "，更改头像后，异步更改动态表、黑名单表，管理员表，评论表的昵称失败！");
                            }
                        }
                    });
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("修改失败！");
        }catch (Exception e){
            logger.error("openId：<" +openId + ": " +  nickName + ">,修改签名失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，修改失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result setGender(String openId, int gender,int signin) {
        Result res = new Result();
        if(gender != Constant.FEMALE_GENDER && gender != Constant.MALE_GENDER){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        if(signin != Constant.LOGIN_IN_WECHAT && signin !=Constant.LOGIN_IN_ACCOUNT){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("登录渠道错误！");
            return res;
        }
        try {
            if(signin == Constant.LOGIN_IN_ACCOUNT){
                boolean p = accountUserDao.setGender(openId, gender);
                if(p){
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }else if(signin == Constant.LOGIN_IN_WECHAT){
                boolean p = wechatUserDao.setGender(openId, gender);
                if(p){
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("修改失败！");
        }catch (Exception e){
            logger.error("openId：<" +openId + ": " +  gender + ">,修改性别失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，修改失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result setCampus(String openId, int campus,int signin) {
        Result res = new Result();
        if(!Constant.CampusEnum.isCampus(campus)){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        if(signin != Constant.LOGIN_IN_WECHAT && signin !=Constant.LOGIN_IN_ACCOUNT){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("登录渠道错误！");
            return res;
        }
        try {
            if(signin == Constant.LOGIN_IN_ACCOUNT){
                boolean p = accountUserDao.setCampus(openId, campus);
                if(p){
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }else if(signin == Constant.LOGIN_IN_WECHAT){
                boolean p = wechatUserDao.setCampus(openId,campus );
                if(p){
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("修改失败！");
        }catch (Exception e){
            logger.error("openId：<" +openId + ": " +  campus + ">,修改校区失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，修改失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result setSignature(String openId, String signature,int signin) {
        Result res = new Result();
        if(signature == null || signature.equals("") || signature.length() > Constant.SIGNATURE_MAX_LENGTH){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        if(signin != Constant.LOGIN_IN_WECHAT && signin !=Constant.LOGIN_IN_ACCOUNT){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("登录渠道错误！");
            return res;
        }
        try {
            if(signin == Constant.LOGIN_IN_ACCOUNT){
                boolean p = accountUserDao.setSignature(openId, signature);
                if(p){
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }else if(signin == Constant.LOGIN_IN_WECHAT){
                boolean p = wechatUserDao.setSignature(openId, signature);
                if(p){
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("修改成功！");
                    return res;
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("修改失败！");
        }catch (Exception e){
            logger.error("openId：<" +openId + ": " +  signature + ">,修改签名失败！");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，修改失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result insertManger(String openId) {
        Result res = new Result();
        try {
            //如果openid小于等于账号最大长度，则说明用户是账号或邮箱登录，否则则为微信登录
            if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                List<AccountUser> list = accountUserDao.getAvatarAndNickName(openId);
                if(!list.isEmpty()){
                    AccountUser user = list.get(0);
                    boolean p = managerDao.insertManger(openId,user.getAvatar(),user.getNickName());
                    if(p){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("申请管理员成功！");
                        return res;
                    }
                }
            }else{
                List<WechatUser> list = wechatUserDao.getAvatarAndNickName(openId);
                if(!list.isEmpty()){
                    WechatUser user = list.get(0);
                    boolean p = managerDao.insertManger(openId,user.getAvatar(),user.getNickName());
                    if(p){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("申请管理员成功！");
                        return res;
                    }
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("申请管理员失败！");
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("申请管理员成功！");
                return res;
            }
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，申请管理员失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result getApplyer() {
        Result res = new Result();
        try {
            List<Manager> list = managerDao.getApplyer();
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("获取数据成功！");
            res.setData(list);
            return res;
        }catch (Exception e) {
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取数据失败！");
        }
        return res;
    }

    @Override
    public Result getManager() {
        Result res = new Result();
        try {
            List<Manager> list = managerDao.getManager();
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("获取数据成功！");
            res.setData(list);
            return res;
        }catch (Exception e) {
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取数据失败！");
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result setManager(String openId) {
        Result res = new Result();
        try {
            boolean p = managerDao.setManager(openId);
            if(p){
                if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                    boolean f = accountUserDao.setManager(openId);
                    if(f){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("添加管理员成功！");
                    }else{
                        throw new Exception();
                    }
                }else{
                    boolean f = wechatUserDao.setManager(openId);
                    if(f){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("添加管理员成功！");
                    }else{
                        throw new Exception();
                    }
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，添加管理员失败！");
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result delManager(String openId) {
        Result res = new Result();
        try {
            boolean p = managerDao.delManager(openId);
            if(p){
                if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                    boolean f = accountUserDao.delManager(openId);
                    if(f){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("取消管理员成功！");
                    }else{
                        throw new Exception();
                    }
                }else{
                    boolean f = wechatUserDao.delManager(openId);
                    if(f){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("取消管理员成功！");
                    }else{
                        throw new Exception();
                    }
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，取消管理员失败！");
        }
        return res;
    }

    @Override
    public Result getUserPage(String openId) {
        Result res = new Result();
        try {
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("获取用户信息成功！");
            if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                List<AccountUser> list = accountUserDao.getUserPage(openId);
                if(!list.isEmpty()){
                    res.setData(list.get(0));
                }else{
                    res.setData(null);
                }
            }else{
                List<WechatUser> list = wechatUserDao.getUserPage(openId);
                if(!list.isEmpty()){
                    res.setData(list.get(0));
                }else{
                    res.setData(null);
                }
            }
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取用户信息失败！");
        }
        return res;
    }

    @Override
    public Result informUser(String openId,String content,String imgs) {
        Result res = new Result();
        if("".equals(content) || content.length() == 0){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        try {
            String imgsUrl = "";
            if(!"".equals(imgs)){
                long timestamp = System.currentTimeMillis();
                String url =  openId + String.valueOf(timestamp)  + ".jpg";
                if(!BASE64DecodedMultipartFile.base64ToMultipart(imgs, Constant.DEFAULT_INFORM_IMG_URL + url)){
                    res.setCode(Constant.REQUEST_FAILED);
                    res.setMessage("举报失败！");
                    return res;
                }
                imgsUrl = url;
            }
            //如果openid小于等于账号最大长度，则说明用户是账号或邮箱登录，否则则为微信登录
            if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                List<AccountUser> list = accountUserDao.getAvatarAndNickName(openId);
                if(!list.isEmpty()){
                    AccountUser user = list.get(0);
                    boolean p = blackUserDao.informUser(openId,user.getAvatar(),user.getNickName());
                    if(p){
                        boolean t = blackReasonDao.informUser(openId, content, imgsUrl);
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("举报成功！");
                        return res;
                    }
                }
            }else{
                List<WechatUser> list = wechatUserDao.getAvatarAndNickName(openId);
                if(!list.isEmpty()){
                    WechatUser user = list.get(0);
                    boolean p = blackUserDao.informUser(openId,user.getAvatar(),user.getNickName());
                    if(p){
                        boolean t = blackReasonDao.informUser(openId, content, imgsUrl);
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("举报成功！");
                        return res;
                    }
                }
            }
            res.setCode(Constant.REQUEST_FAILED);
            res.setMessage("举报失败！");
        }catch (Exception e){
            if(e instanceof DuplicateKeyException){
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("举报成功！");
                return res;
            }
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，举报失败！");
            return res;
        }
        return res;
    }

    @Override
    public Result getInformedUser() {
        Result res = new Result();
        try {
            List<BlackUser> list = blackUserDao.getInformedUser();
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("获取被举报用户列表成功！");
            res.setData(list);
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取失败！");
        }
        return res;
    }

    @Override
    public Result getInformReason(String openId) {
        Result res = new Result();
        try {
            List<BlackReason>  list = blackReasonDao.getInformReason(openId);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setImg(Constant.INFORM_SERVER_URL + list.get(i).getImg());
            }
            res.setData(list);
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("获取举报原因成功！");
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取失败！");
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delInformedUser(String openId) {
        Result res = new Result();
        try {
            List<BlackReason> list = blackReasonDao.getImg(openId);
            if(list.isEmpty()){
                res.setCode(Constant.NO_DATA_IN_MYSQL);
                res.setMessage("数据库没有该用户信息");
                return res;
            }
            boolean p = blackUserDao.delInformedUser(openId);
            if(p){
                boolean t = blackReasonDao.delInformReason(openId);
                if(t){
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for(int i = 0; i < list.size(); i++){
                                    if(!"".equals(list.get(i).getImg())){
                                        String img = Constant.DEFAULT_INFORM_IMG_URL + list.get(i).getImg();
                                        new File(img).delete();
                                    }
                                }
                            }catch(Exception e){
                                logger.info("用户：" + openId + "，删除举报信息后，异步删除对应图片失败！");
                            }
                        }
                    });
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("删除成功！");
                }else{
                    throw new Exception();
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，删除失败！");
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result lockUser(String openId) {
        Result res = new Result();
        try {
            boolean p = blackUserDao.lockUser(openId);
            if(p){
                if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                    boolean t = accountUserDao.lockUser(openId);
                    if(t){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("封禁成功！");
                    }else{
                        throw new Exception();
                    }
                }else{
                    boolean t = wechatUserDao.lockUser(openId);
                    if(t){
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("封禁成功！");
                    }else{
                        throw new Exception();
                    }
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，封号失败！");
        }
        return res;
    }

    @Override
    public Result getLockedUser() {
        Result res = new Result();
        try {
            List<BlackUser>  list = blackUserDao.getLockedUser();
            res.setData(list);
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("获取封禁用户名单成功！");
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取封禁用户名单失败！");
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result unlockUser(String openId) {
        Result res = new Result();
        try {
            List<BlackReason> list = blackReasonDao.getImg(openId);
            if(list.isEmpty()){
                res.setCode(Constant.NO_DATA_IN_MYSQL);
                res.setMessage("数据库没有该用户信息");
                return res;
            }
            boolean p = false;
            if(openId.length() <= Constant.MAX_ACCOUNT_LENGTH){
                p = accountUserDao.unlockUser(openId);
            }else{
                p = wechatUserDao.unlockUser(openId);
            }
            if(p){
                boolean t = blackUserDao.unlockUser(openId);
                if(t){
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for(int i = 0; i < list.size(); i++){
                                    if(!"".equals(list.get(i).getImg())){
                                        String img = Constant.DEFAULT_INFORM_IMG_URL + list.get(i).getImg();
                                        new File(img).delete();
                                    }
                                }
                            }catch(Exception e){
                                logger.info("用户：" + openId + "，删除举报信息后，异步删除对应图片失败！");
                            }
                        }
                    });
                    blackReasonDao.delInformReason(openId);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("解封用户成功！");
                }else{
                    throw new Exception();
                }
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，解封用户失败！");
        }
        return res;
    }


}

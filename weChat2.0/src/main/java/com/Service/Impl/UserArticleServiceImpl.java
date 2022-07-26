package com.Service.Impl;

import com.DAO.*;
import com.Entity.AccountUser;
import com.Entity.UserArticleWait;
import com.Entity.WechatUser;
import com.Service.UserArticleService;
import com.Utils.BASE64DecodedMultipartFile;
import com.Utils.Constant;
import com.VO.Article;
import com.VO.Result;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;


@Service
@Log4j2
public class UserArticleServiceImpl implements UserArticleService {

    public final static Logger logger = LoggerFactory.getLogger(UserArticleServiceImpl.class);

    @Autowired
    private UserWaitArticleDao userWaitArticleDao;

    @Autowired
    private AccountUserDao accountUserDao;

    @Autowired
    private WechatUserDao wechatUserDao;

    @Autowired
    private ArticlesDao articlesDao;

    @Autowired
    private SurfaceDao surfaceDao;

    @Autowired
    private Executor executor;

    @Autowired
    private RedisTemplate<String, Object> redisTemplateHash;

    @Override
    public Result submitArticle(String openId,int signin,String content, List<String> imgs,int color) {
        Result res = new Result();
        if(content == null || content.trim().equals("")){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        if(signin != Constant.LOGIN_IN_WECHAT && signin !=Constant.LOGIN_IN_ACCOUNT){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("登录渠道错误！");
            return res;
        }
        if(imgs.size() > Constant.USER_CONTENT_IMG_MAX_LENGTH || !Constant.ColorEnum.isColor(color)){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数不合格！");
            return res;
        }
        String imgsUrl = "";
        long timestamp = System.currentTimeMillis();;
        if(!imgs.isEmpty()){
            for(int i = 0; i < imgs.size(); i++){
                String url =  openId + String.valueOf(timestamp) + i + Constant.ARTICLE_IMG_END_KEY;
                imgsUrl += url + ",";
                if(BASE64DecodedMultipartFile.base64ToMultipart(imgs.get(i), Constant.DEFAULT_ARTICLE_IMGS_URL + url)){
                   continue;
                }else{
                    res.setCode(Constant.REQUEST_FAILED);
                    res.setMessage("发布动态失败！");
                    return res;
                }
            }
        }
        String avatar = "";
        String nickName = "";
        try {
            if(signin == Constant.LOGIN_IN_ACCOUNT){
                List<AccountUser> list = accountUserDao.getAvatarAndNickName(openId);
                if(list.isEmpty()){
                    res.setCode(Constant.REQUEST_FAILED);
                    res.setMessage("发布动态失败！");
                    return res;
                }
                avatar = list.get(0).getAvatar();
                nickName = list.get(0).getNickName();
            }else if(signin == Constant.LOGIN_IN_WECHAT){
                List<WechatUser> list = wechatUserDao.getAvatarAndNickName(openId);
                if(list.isEmpty()){
                    res.setCode(Constant.REQUEST_FAILED);
                    res.setMessage("发布动态失败！");
                    return res;
                }
                avatar = list.get(0).getAvatar();
                nickName = list.get(0).getNickName();
            }
            boolean p = userWaitArticleDao.submitArticle(openId,avatar,nickName,content,imgsUrl,timestamp,color);
            if(p){
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("发布动态成功！");
            }else{
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("发布动态失败！");
            }
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    public Result getAllWaitArticle() {
        Result res = new Result();
        try {
            List<UserArticleWait> list = userWaitArticleDao.getAllWaitArticle(Constant.ARTICLE_STATE);
            List<Article> list1 = new ArrayList<>();
            if(!list.isEmpty()){
                for(int i = 0; i < list.size(); i++){
                    Article article = new Article();
                    if(list.get(i).getImgs() == null || "".equals(list.get(i).getImgs())){
                        String[] s = new String[0];
                        article.setImgs(s);
                    }else{
                        String[] img = list.get(i).getImgs().split(",");
                        for(int j = 0; j < img.length; j++){
                            img[j] = Constant.ARTICLE_SERVER_URL + img[j];
                        }
                        article.setImgs(img);
                    }
                    article.setId(list.get(i).getArticleId());
                    article.setOpenId(list.get(i).getOpenId());
                    article.setAvatar(list.get(i).getAvatar());
                    article.setNickName(list.get(i).getNickName());
                    article.setContent(list.get(i).getContent());
                    String str = "";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getTimestamp());
                    str = simpleDateFormat.format(date);
                    article.setTimestamp(str);
                    article.setColor(list.get(i).getColor());
                    list1.add(article);
                }
            }
            HashMap<String,Object> map = new HashMap<>();
            map.put("list",list1);
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("加载数据成功！");
            res.setData(map);
        }catch (Exception e){
            res.setData("");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    public Result getArticles() {
        Result res = new Result();
        try {
            List<UserArticleWait> list = userWaitArticleDao.getAllWaitArticle(Constant.CHOOSE_ARTICLE_STATE);
            List<Article> list1 = new ArrayList<>();
            if(!list.isEmpty()){
                for(int i = 0; i < list.size(); i++){
                    Article article = new Article();
                    if(list.get(i).getImgs() == null || "".equals(list.get(i).getImgs())){
                        String[] s = new String[0];
                        article.setImgs(s);
                    }else{
                        String[] img = list.get(i).getImgs().split(",");
                        for(int j = 0; j < img.length; j++){
                            img[j] = Constant.ARTICLE_SERVER_URL + img[j];
                        }
                        article.setImgs(img);
                    }
                    article.setId(list.get(i).getArticleId());
                    article.setOpenId(list.get(i).getOpenId());
                    article.setAvatar(list.get(i).getAvatar());
                    article.setNickName(list.get(i).getNickName());
                    article.setContent(list.get(i).getContent());
                    String str = "";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(list.get(i).getTimestamp());
                    str = simpleDateFormat.format(date);
                    article.setTimestamp(str);
                    article.setColor(list.get(i).getColor());
                    list1.add(article);
                }
            }
            HashMap<String,Object> map = new HashMap<>();
            map.put("list",list1);
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("加载数据成功！");
            res.setData(map);
        }catch (Exception e){
            res.setData("");
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    public Result chooseArticle(int articleId) {
        Result res = new Result();
        try {
            boolean p = userWaitArticleDao.chooseArticle(articleId,Constant.CHOOSE_ARTICLE_STATE);
            if(p){
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("选择成功！");
            }else{
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("选择失败！");
            }
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    public Result setRed(int articleId) {
        Result res = new Result();
        try {
            boolean p = userWaitArticleDao.setRed(articleId,Constant.RED_COLOR);
            if(p){
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("操作成功！");
            }else{
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("操作失败！");
            }
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    public Result hideArticle(int articleId) {
        Result res = new Result();
        try {
            List<UserArticleWait> list = userWaitArticleDao.getImgs(articleId);
            if(list.isEmpty()){
                res.setCode(Constant.NO_DATA_IN_MYSQL);
                res.setMessage("数据库没有该信息");
                return res;
            }
            boolean p = userWaitArticleDao.hideArticle(articleId,Constant.HIDE_IMGS,Constant.HIDE_CONTENT);
            if(p){
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] img = list.get(0).getImgs().split(",");
                            for(int j = 0; j < img.length; j++){
                                if("".equals(img[j])){
                                    continue;
                                }
                                img[j] = Constant.DEFAULT_ARTICLE_IMGS_URL + img[j];
                                new File(img[j]).delete();
                            }
                        }catch(Exception e){
                            logger.info("动态：" + articleId + "，屏蔽动态，异步删除图片文件失败！");
                        }
                    }
                });
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("已屏蔽！");
            }else{
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("屏蔽失败！");
            }
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    public Result delArticleById(int articleId) {
        Result res = new Result();
        try {
            List<UserArticleWait> list = userWaitArticleDao.getImgs(articleId);
            if(list.isEmpty()){
                res.setCode(Constant.NO_DATA_IN_MYSQL);
                res.setMessage("数据库没有该信息");
                return res;
            }
            boolean p = userWaitArticleDao.delArticleById(articleId);
            if(p){
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] img = list.get(0).getImgs().split(",");
                            for(int j = 0; j < img.length; j++){
                                if("".equals(img[j])){
                                    continue;
                                }
                                img[j] = Constant.DEFAULT_ARTICLE_IMGS_URL + img[j];
                                new File(img[j]).delete();
                            }
                        }catch(Exception e){
                            logger.info("动态：" + articleId + "，删除动态，异步删除图片文件失败！");
                        }
                    }
                });
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("删除成功！");
            }else{
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("删除失败！");
            }
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，提交失败！");
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result publishArticle(MultipartFile file, String content) {
        Result res = new Result();
        if(file.isEmpty() || content.isEmpty()){
            res.setCode(Constant.PARAM_ERROR);
            res.setMessage("参数错误！");
            return res;
        }
        try {
            String allName = file.getOriginalFilename();
            String name = System.currentTimeMillis() + allName.substring(allName.lastIndexOf("."));
            String img = Constant.SURFACE_SERVER_URL + name;
            try {
                file.transferTo(new File(Constant.DEFAULT_SURFACE_IMGS_URL + name));
            }catch (Exception e){
                res.setCode(Constant.REQUEST_FAILED);
                res.setMessage("发布失败！");
                return res;
            }
            boolean p = surfaceDao.insertSurface(img,content,System.currentTimeMillis());
            int id = surfaceDao.getId();
            List<UserArticleWait> list = userWaitArticleDao.getAllWaitArticle(Constant.CHOOSE_ARTICLE_STATE);
            if(!list.isEmpty()){
                for(int i = 0; i < list.size(); i++){
                    articlesDao.publishArticle(id,list.get(i).getOpenId(),list.get(i).getAvatar(),
                            list.get(i).getNickName(),list.get(i).getContent(),list.get(i).getImgs(),
                            list.get(i).getTimestamp(),list.get(i).getColor());
                }
                for(int i = 0; i < list.size(); i++){
                    userWaitArticleDao.delArticleById(list.get(i).getArticleId());
                }
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        Constant.FRESH_ARTICLE_ID = -1;
                        if(!Constant.REDIS_IS_DOWN){
                            synchronized (Constant.SURFACE_LOCK){
                                if(!redisTemplateHash.hasKey(Constant.SURFACES_LIST_KEY)){
                                    redisTemplateHash.delete(Constant.SURFACES_LIST_KEY);
                                }
                            }
                        }
                    }catch (Exception e){
                        if(e instanceof RedisConnectionFailureException){
                            logger.error("redis 已宕机！");
                            Constant.REDIS_IS_DOWN = true;
                        }
                    }
                }
            });
            res.setCode(Constant.REQUEST_SUCCESS);
            res.setMessage("发布成功！");
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，发布失败！");
        }
        return res;
    }


}

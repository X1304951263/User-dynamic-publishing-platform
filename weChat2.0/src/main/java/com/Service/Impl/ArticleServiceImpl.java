package com.Service.Impl;

import com.DAO.ArticlesDao;
import com.DAO.CommentDao;
import com.DAO.LoveArticlesDao;
import com.DAO.SurfaceDao;
import com.Entity.Articles;
import com.Entity.Comment;
import com.Entity.LoveArticles;
import com.Entity.Surface;
import com.Service.ArticleService;
import com.Utils.Constant;
import com.VO.Article;
import com.VO.Result;
import com.VO.SurfaceVO;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class ArticleServiceImpl implements ArticleService {
    public final static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplateHash;

    @Autowired
    private Executor executor;

    @Autowired
    private ArticlesDao articlesDao;

    @Autowired
    private SurfaceDao surfaceDao;

    @Autowired
    private LoveArticlesDao loveArticlesDao;

    @Autowired
    private CommentDao commentDao;

    @Override
    public Result getArticles(String openId) {
        Result res = new Result();
        try {
            if(Constant.FRESH_ARTICLE_ID == -1){
                if(surfaceDao.getId() == null){
                    Constant.FRESH_ARTICLE_ID = 1;
                }else{
                    Constant.FRESH_ARTICLE_ID = surfaceDao.getId();
                }
            }
            Surface surface = surfaceDao.getFreshedSurface(Constant.FRESH_ARTICLE_ID);
            String str = "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            List<Articles> list = articlesDao.getArticlesById(Constant.FRESH_ARTICLE_ID);
            List<LoveArticles> loves = loveArticlesDao.getLoves(openId, Constant.FRESH_ARTICLE_ID);
            HashSet<Integer> set = new HashSet<>();
            for(int i = 0; i < loves.size(); i++){
                set.add(loves.get(i).getArticleId());
            }
            List<Article> list1 = new ArrayList<>();
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
                if(set.contains(list.get(i).getArticleId())){
                    article.setIsLove(1);
                }else{
                    article.setIsLove(0);
                }
                article.setId(list.get(i).getArticleId());
                article.setOpenId(list.get(i).getOpenId());
                article.setAvatar(list.get(i).getAvatar());
                article.setNickName(list.get(i).getNickName());
                article.setContent(list.get(i).getContent());
                article.setLove(list.get(i).getLove());
                article.setComment(list.get(i).getComment());
                date = new Date(list.get(i).getTimestamp());
                str = simpleDateFormat.format(date);
                article.setTimestamp(str);
                article.setColor(list.get(i).getColor());
                list1.add(article);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("surface",surface);
            map.put("articles", list1);
            res.setData(map);
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取文案失败！");
            res.setData("");
        }
        return res;
    }

    @Override
    public Result getArticlesBySurfaceId(String openId, int id) {
        Result res = new Result();
        try {
            Surface surface = surfaceDao.getFreshedSurface(id);
            String str = "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            List<Articles> list = articlesDao.getArticlesById(id);
            List<LoveArticles> loves = loveArticlesDao.getLoves(openId, id);
            HashSet<Integer> set = new HashSet<>();
            for(int i = 0; i < loves.size(); i++){
                set.add(loves.get(i).getArticleId());
            }
            List<Article> list1 = new ArrayList<>();
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
                if(set.contains(list.get(i).getArticleId())){
                    article.setIsLove(1);
                }else{
                    article.setIsLove(0);
                }
                article.setId(list.get(i).getArticleId());
                article.setOpenId(list.get(i).getOpenId());
                article.setAvatar(list.get(i).getAvatar());
                article.setNickName(list.get(i).getNickName());
                article.setContent(list.get(i).getContent());
                article.setLove(list.get(i).getLove());
                article.setComment(list.get(i).getComment());
                date = new Date(list.get(i).getTimestamp());
                str = simpleDateFormat.format(date);
                article.setTimestamp(str);
                article.setColor(list.get(i).getColor());
                list1.add(article);
            }
            Map<String,Object> map = new HashMap<>();
            map.put("surface",surface);
            map.put("articles", list1);
            res.setData(map);
        }catch (Exception e){
            res.setCode(Constant.SERVICE_ERROR);
            res.setMessage("服务出错，获取文案失败！");
            res.setData("");
        }
        return res;
    }

    @Override
    public Result getAllSurface() {
        Result res = new Result();
        try {
            /*if(!Constant.REDIS_IS_DOWN){
                if(redisTemplateHash.hasKey(Constant.SURFACES_LIST_KEY)){
                    List list = redisTemplateHash.opsForList().range(Constant.SURFACES_LIST_KEY,0,-1);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("获取文案数据成功！");
                    res.setData(list);
                }else{
                    List<Surface> list = surfaceDao.getAllSurface();
                    List<SurfaceVO> list1 = new ArrayList<>();
                    if(!list.isEmpty()) {
                        for (Surface surface : list) {
                            SurfaceVO s = new SurfaceVO();
                            s.setId(surface.getId());
                            s.setImg(surface.getImg());
                            s.setContent(surface.getContent());
                            String str = "";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(surface.getTimestamp());
                            str = simpleDateFormat.format(date);
                            s.setTimestamp(str);
                            list1.add(s);
                        }
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    synchronized (Constant.SURFACE_LOCK){
                                        if(Constant.FRESH_ARTICLE_ID > -1){
                                            if(!redisTemplateHash.hasKey(Constant.SURFACES_LIST_KEY)){
                                                for(int i = list1.size() - 1; i >= 0; i--){
                                                    redisTemplateHash.opsForList().leftPush(Constant.SURFACES_LIST_KEY, list1.get(i));
                                                }
                                                redisTemplateHash.expire(Constant.SURFACES_LIST_KEY,Constant.SURFACES_EXPIRE_TIME,TimeUnit.SECONDS);
                                            }
                                        }
                                    }
                                }catch (Exception e){
                                    if(e instanceof RedisConnectionFailureException){
                                        logger.error("redis 已宕机！");
                                        Constant.REDIS_IS_DOWN = true;
                                    }
                                    redisTemplateHash.delete(Constant.SURFACES_LIST_KEY);
                                    logger.error("异步将封面列表写入redis失败！");
                                }
                            }
                        });
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("获取文案数据成功！");
                        res.setData(list1);
                    }
                }
            }else{*/
                List<Surface> list = surfaceDao.getAllSurface();
                List<SurfaceVO> list1 = new ArrayList<>();
                if(!list.isEmpty()){
                    for(Surface surface : list){
                        SurfaceVO s = new SurfaceVO();
                        s.setId(surface.getId());
                        s.setImg(surface.getImg());
                        s.setContent(surface.getContent());
                        String str = "";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(surface.getTimestamp());
                        str = simpleDateFormat.format(date);
                        s.setTimestamp(str);
                        list1.add(s);
                    }
                }
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("获取文案数据成功！");
                res.setData(list1);
            //}
        }catch (Exception e){
            if(e instanceof RedisConnectionFailureException){
                logger.error("redis 已宕机！");
                Constant.REDIS_IS_DOWN = true;
                try {
                    List<Surface> list = surfaceDao.getAllSurface();
                    List<SurfaceVO> list1 = new ArrayList<>();
                    if(!list.isEmpty()){
                        for(Surface surface : list){
                            SurfaceVO s = new SurfaceVO();
                            s.setId(surface.getId());
                            s.setImg(surface.getImg());
                            s.setContent(surface.getContent());
                            String str = "";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(surface.getTimestamp());
                            str = simpleDateFormat.format(date);
                            s.setTimestamp(str);
                            list1.add(s);
                        }
                    }
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("获取文案数据成功！");
                    res.setData(list1);
                }catch (Exception e1){
                    res.setCode(Constant.SERVICE_ERROR);
                    res.setMessage("服务出错，获取文案失败！");
                }
            }else{
                res.setCode(Constant.SERVICE_ERROR);
                res.setMessage("服务出错，获取文案失败！");
            }
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result loveArticle(String openId, int articleId, int id) {
        Result res = new Result();
        if(!Constant.REDIS_IS_DOWN){
            try {
                if(redisTemplateHash.hasKey(Constant.ARTICLEID_LOVE_HASH + articleId)){
                    if(redisTemplateHash.opsForHash().hasKey(Constant.ARTICLEID_LOVE_HASH + articleId,openId)){
                        long num = redisTemplateHash.opsForHash().delete(Constant.ARTICLEID_LOVE_HASH + articleId,openId);
                        if(num > 0){
                            res.setCode(Constant.REQUEST_SUCCESS);
                            res.setMessage("取消点赞成功！");
                            //这里异步取消数据库的点赞
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        int num = articlesDao.getLovesNum(articleId);
                                        articlesDao.setLove(articleId,num - 1);
                                        loveArticlesDao.delLove(articleId, openId);
                                    }catch(Exception e){
                                        //取消点赞失败
                                        logger.error("异步取消数据库点赞失败！");
                                    }
                                }
                            });
                        }else{
                            res.setCode(Constant.REQUEST_FAILED);
                            res.setMessage("取消点赞失败！");
                        }
                        return res;
                    }else{
                        redisTemplateHash.opsForHash().put(Constant.ARTICLEID_LOVE_HASH + articleId,openId,1);
                        //这里异步增加数据库的点赞
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    int num = articlesDao.getLovesNum(articleId);
                                    articlesDao.setLove(articleId,num + 1);
                                    loveArticlesDao.insertLove(articleId, openId, id);
                                }catch(Exception e){
                                    logger.error("异步增加数据库点赞失败！");
                                }
                            }
                        });
                        res.setCode(Constant.REQUEST_SUCCESS);
                        res.setMessage("点赞成功！");
                        return res;
                    }
                }else{
                    //点赞/取消点赞数据库,并异步将当前articleid的动态的点赞列表添加至缓存中
                    List<LoveArticles> list = loveArticlesDao.getLoveByOpenId(articleId, openId);
                    if(list.isEmpty()){
                        int num = articlesDao.getLovesNum(articleId);
                        articlesDao.setLove(articleId,num + 1);
                        loveArticlesDao.insertLove(articleId, openId, id);
                    }else{
                        int num = articlesDao.getLovesNum(articleId);
                        articlesDao.setLove(articleId,num - 1);
                        loveArticlesDao.delLove(articleId, openId);
                    }
                    try {
                        List<LoveArticles> loves = loveArticlesDao.getArticleLoves(articleId);
                        if (!loves.isEmpty()){
                            for(LoveArticles i : loves){
                                redisTemplateHash.opsForHash().put(Constant.ARTICLEID_LOVE_HASH + articleId,
                                        i.getOpenId(),1);
                            }
                            redisTemplateHash.expire(Constant.ARTICLEID_LOVE_HASH + articleId,30 * 60, TimeUnit.SECONDS);
                        }
                    }catch(Exception e){
                        if(e instanceof RedisConnectionFailureException){
                            logger.error("redis 已宕机！");
                            Constant.REDIS_IS_DOWN = true;
                        }
                        redisTemplateHash.delete(Constant.ARTICLEID_LOVE_HASH + articleId);
                        logger.error("取消/点赞写入缓存失败！");
                    }
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("取消/点赞成功！");
                }
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    Constant.REDIS_IS_DOWN = true;
                    loveArticle(openId, articleId, id);
                }else{
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键
                    res.setCode(Constant.SERVICE_ERROR);
                    res.setMessage("服务出错，修改失败！");
                    return res;
                }
            }
        }else{
            try {
                List<LoveArticles> list = loveArticlesDao.getLoveByOpenId(articleId, openId);
                if(list.isEmpty()){
                    int num = articlesDao.getLovesNum(articleId);
                    articlesDao.setLove(articleId,num + 1);
                    loveArticlesDao.insertLove(articleId, openId, id);
                }else{
                    int num = articlesDao.getLovesNum(articleId);
                    articlesDao.setLove(articleId,num - 1);
                    loveArticlesDao.delLove(articleId, openId);
                }
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("取消/点赞成功！");
                return res;
            }catch (Exception e){
                res.setCode(Constant.SERVICE_ERROR);
                res.setMessage("服务出错，修改失败！");
                return res;
            }
        }
        return res;
    }

    @Override
    public Result getComments(int articleId) {
        Result res = new Result();
        if(!Constant.REDIS_IS_DOWN){
            try {
                if(redisTemplateHash.hasKey(Constant.ARTICLEID_CONMENT_LIST + articleId)){
                    //缓存有该动态信息，直接从缓存读取并返回
                    List list = redisTemplateHash.opsForList().range(Constant.ARTICLEID_CONMENT_LIST + articleId,0,-1);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("获取评论成功！");
                    res.setData(list);
                    return  res;
                }else{
                    List<Comment> list = commentDao.getComments(articleId);
                    //如果缓存没有该动态的评论，就异步加入缓存
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (Constant.COMMENT_LOCK){
                                    if(!redisTemplateHash.hasKey(Constant.ARTICLEID_CONMENT_LIST + articleId)){
                                        for(int i = list.size() - 1; i >= 0; i--){
                                            redisTemplateHash.opsForList().leftPush(Constant.ARTICLEID_CONMENT_LIST + articleId, list.get(i));
                                        }
                                        redisTemplateHash.expire(Constant.ARTICLEID_CONMENT_LIST + articleId,Constant.SURFACES_EXPIRE_TIME,TimeUnit.SECONDS);
                                    }
                                }
                            }catch (Exception e){
                                if(e instanceof RedisConnectionFailureException){
                                    logger.error("redis 已宕机！");
                                    Constant.REDIS_IS_DOWN = true;
                                }
                                redisTemplateHash.delete(Constant.ARTICLEID_CONMENT_LIST + articleId);
                                logger.error("异步将动态的评论列表写入redis失败！");
                            }
                        }
                    });
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("获取评论成功！");
                    res.setData(list);
                    return  res;
                }
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    Constant.REDIS_IS_DOWN = true;
                }
                try {
                    List<Comment> list = commentDao.getComments(articleId);
                    res.setCode(Constant.REQUEST_SUCCESS);
                    res.setMessage("获取评论成功！");
                    res.setData(list);
                    return  res;
                }catch (Exception e1){
                    res.setCode(Constant.SERVICE_ERROR);
                    res.setMessage("服务出错，获取评论信息失败！");
                    return res;
                }
            }
        }else{
           try {
               List<Comment> list = commentDao.getComments(articleId);
               res.setCode(Constant.REQUEST_SUCCESS);
               res.setMessage("获取评论成功！");
               res.setData(list);
               return  res;
           }catch (Exception e){
               res.setCode(Constant.SERVICE_ERROR);
               res.setMessage("服务出错，获取评论信息失败！");
           }
        }
        return res;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result insertComment(String openId_one,String nickName_one,String openId_two,String nickName_two
            ,String content,int articleId) {

        Result res = new Result();
        if(!Constant.REDIS_IS_DOWN){
            try {
                if(redisTemplateHash.hasKey(Constant.ARTICLEID_CONMENT_LIST + articleId)){
                    redisTemplateHash.delete(Constant.ARTICLEID_CONMENT_LIST + articleId);
                }
                commentDao.insertComment(openId_one,nickName_one,openId_two,nickName_two,content,articleId);
                int num = articlesDao.getCommentsNum(articleId);
                num++;
                articlesDao.setComment(articleId,num);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            if(redisTemplateHash.hasKey(Constant.ARTICLEID_CONMENT_LIST + articleId)){
                                redisTemplateHash.delete(Constant.ARTICLEID_CONMENT_LIST + articleId);
                            }
                        }catch (Exception e){
                            if(e instanceof RedisConnectionFailureException){
                                Constant.REDIS_IS_DOWN = true;
                                logger.error("redis已宕机！");
                            }
                            logger.error("用户添加评论/回复后，延迟双删缓存失败！");
                        }
                    }
                });
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("评论成功！");
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    Constant.REDIS_IS_DOWN = true;
                    logger.error("redis已宕机！");
                }
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                res.setCode(Constant.SERVICE_ERROR);
                res.setMessage("服务出错，插入评论信息失败！");
                commentDao.insertComment(openId_one,nickName_one,openId_two,nickName_two,content,articleId);
                int num = articlesDao.getCommentsNum(articleId);
                num++;
                articlesDao.setComment(articleId,num);
            }
        //redis 宕机
        }else{
            try {
                commentDao.insertComment(openId_one,nickName_one,openId_two,nickName_two,content,articleId);
                int num = articlesDao.getCommentsNum(articleId);
                num++;
                boolean p = articlesDao.setComment(articleId,num);
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("评论成功！");
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                res.setCode(Constant.SERVICE_ERROR);
                res.setMessage("服务出错，插入评论信息失败！");
            }
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result delComment(int id, String openId, int articleId) {
        Result res = new Result();
        if(!Constant.REDIS_IS_DOWN){
            try {
                if(redisTemplateHash.hasKey(Constant.ARTICLEID_CONMENT_LIST + articleId)){
                    redisTemplateHash.delete(Constant.ARTICLEID_CONMENT_LIST + articleId);
                }
                commentDao.delComment(id, openId, articleId);
                int num = articlesDao.getCommentsNum(articleId);
                num--;
                articlesDao.setComment(articleId,num);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            if(redisTemplateHash.hasKey(Constant.ARTICLEID_CONMENT_LIST + articleId)){
                                redisTemplateHash.delete(Constant.ARTICLEID_CONMENT_LIST + articleId);
                            }
                        }catch (Exception e){
                            if(e instanceof RedisConnectionFailureException){
                                Constant.REDIS_IS_DOWN = true;
                                logger.error("redis已宕机！");
                            }
                            logger.error("用户删除评论/回复后，延迟双删缓存失败！");
                        }
                    }
                });
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("删除评论成功！");
            }catch (Exception e){
                if(e instanceof RedisConnectionFailureException){
                    Constant.REDIS_IS_DOWN = true;
                    delComment(id, openId, articleId);
                    logger.error("redis已宕机！");
                }
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                res.setCode(Constant.SERVICE_ERROR);
                res.setMessage("服务出错，插入评论信息失败！");
                commentDao.delComment(id, openId, articleId);
                int num = articlesDao.getCommentsNum(articleId);
                num--;
                articlesDao.setComment(articleId,num);
            }
            //redis 宕机
        }else{
            try {
                commentDao.delComment(id, openId, articleId);
                int num = articlesDao.getCommentsNum(articleId);
                num--;
                boolean p = articlesDao.setComment(articleId,num);
                res.setCode(Constant.REQUEST_SUCCESS);
                res.setMessage("删除评论成功！");
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                res.setCode(Constant.SERVICE_ERROR);
                res.setMessage("服务出错，删除评论信息失败！");
            }
        }
        return res;
    }
}

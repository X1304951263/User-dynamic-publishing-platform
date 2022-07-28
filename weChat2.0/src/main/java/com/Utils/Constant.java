package com.Utils;

import java.io.File;

/**
 * @author 13049
 */
public class Constant {
    public static int USER_AUTH = 0;   //普通用户的角色位
    public static int MANAGER_AUTH = 1;  //管理员的角色位
    public static int ADMIN_AUTH = 2;   //超级管理员的角色位

    public static int APPLY_MANAGER = 0; // 用户申请管理员初始状态位
    public static int IS_MANAGER = 1;  //添加管理员后状态位

    public static int USER_IN_WHITE = 0;   //用户未被封号的状态位
    public static int USER_IN_BLACK = 1;   //用户被封号的状态位

    public static int MIN_ACCOUNT_LENGTH = 6;  //账号注册的用户账号最短为6位
    public static int MAX_ACCOUNT_LENGTH = 14; //账号注册的用户账号最长为14位
    public static int MIN_WORD_LENGTH = 6;   //账户密码最短为6位
    public static int MAX_WORD_LENGTH = 14;  //账户密码最短为14位

    public static int VIRIFYCODE_LENGTH = 6;  //生成的数字验证码长度，默认为6
    public static int VIRIFYCODE_COUNT = 30;  //用户注册账号使用的邮箱验证码阈值，一天默认30次
    public static String PRE_REGISTER_VIRIFYCODE_COUNT_KEY = "CODE_COUNT";  //账户注册的邮箱验证码次数的key前缀,使用:PRE_VIRIFYCODE_KEY + email
    public static long VIRIFYCODE_COUNT_TIME = 24 * 60 * 60; //验证码次数存在时间，默认为一天内不能超过阈值
    public static String PRE_REGISTER_VIRIFYCODE_NUMBER_KEY = "CODE_NUMBER";  //账户注册的邮箱验证码的KEY前缀
    public static long VIRIFYCODE_EXPIRE_TIME = 5 * 60; //验证码存在时间

    public static final int LOGIN_IN_ACCOUNT = 1;  //登录渠道为账号或邮箱
    public static final int LOGIN_IN_WECHAT = 0;   //登录渠道为微信一键登录

    public static int NICKNAME_MAX_LENGTH = 8;   //昵称最长为8
    public static int SIGNATURE_MAX_LENGTH = 14;  //签名最长为14

    public static int MALE_GENDER = 0; //男
    public static int FEMALE_GENDER = 1;  //女

    public static int USER_ARRTICLE_WAIT_STATE = 0;  //未审核的动态状态位
    public static int USER_ARTICLE_READY_STATE = 1;  //审核过关的动态状态位

    //账户注册用户默认头像地址
    /*public static String DEFAULT_AVATAR_URL = "http://xxx/avatar/1.jpg";
    public static String AVATAR_URL = "/usr/local/nginx/images/avatar/"; //头像存储的本地地址
    public static String AVATAR_SERVER_URL = "http://xxx/avatar/";  //用户头像访问位置
    public static String DEFAULT_ARTICLE_IMGS_URL = "/usr/local/nginx/images/article/"; //用户动态图片存储位置
    public static String ARTICLE_SERVER_URL = "http://xxx/article/"; //用户动态图片访问地址
    public static String DEFAULT_SURFACE_IMGS_URL = "/usr/local/nginx/images/surface/"; //用户动态图片存储位置
    public static String SURFACE_SERVER_URL = "http://xxx/surface/"; //封面图片访问地址
    public static String DEFAULT_INFORM_IMG_URL = "/usr/local/nginx/images/inform/"; //用户动态图片存储位置
    public static String INFORM_SERVER_URL = "http://xxx/inform/"; //封面图片访问地址*/

    public static String DEFAULT_AVATAR_URL = "http://127.0.0.1:8081/avatar/1.jpg";
    public static String AVATAR_URL = "D:\\BaiduNetdiskDownload\\nginx-1.20.1\\nginx-1.20.1\\images\\avatar\\"; //头像存储的本地地址
    public static String AVATAR_SERVER_URL = "http://127.0.0.1:8081/avatar/";  //用户头像访问位置
    public static String DEFAULT_ARTICLE_IMGS_URL = "D:\\BaiduNetdiskDownload\\nginx-1.20.1\\nginx-1.20.1\\images\\article\\"; //用户动态图片存储位置
    public static String ARTICLE_SERVER_URL = "http://127.0.0.1:8081/article/"; //用户动态图片访问地址
    public static String DEFAULT_SURFACE_IMGS_URL = "D:\\BaiduNetdiskDownload\\nginx-1.20.1\\nginx-1.20.1\\images\\surface\\"; //用户动态图片存储位置
    public static String SURFACE_SERVER_URL = "http://127.0.0.1:8081/surface/"; //封面图片访问地址
    public static String DEFAULT_INFORM_IMG_URL = "D:\\BaiduNetdiskDownload\\nginx-1.20.1\\nginx-1.20.1\\images\\inform\\"; //用户动态图片存储位置
    public static String INFORM_SERVER_URL = "http://127.0.0.1:8081/inform/"; //封面图片访问地址

    public static final int USER_CONTENT_IMG_MAX_LENGTH = 3;  //用户动态图片最多3张
    public static final String ARTICLE_IMG_END_KEY = ".jpg";  //图片默认后缀格式
    public static String DEFAULT_NICKNAME = "官御天";  //默认昵称
    public static int DEFAULT_GENDER = 0;    //默认性别，男
    public static int DEFAULT_CAMPUS = 0;    //默认校区，小营
    public static String DEFAULT_SIGNATURE = "一晌贪欢，亦是虚幻。";  //默认个性签名

    //同步锁对象
    public static volatile Object SURFACE_LOCK = 1; //封面对象锁
    public static volatile Object COMMENT_LOCK = 1; //评论对象锁

    //缓存
    public static boolean REDIS_IS_DOWN = false;  //缓存宕机标志，默认为false,如果宕机,捕获异常改为true,预留接口,redis启动后人工调整为false
    public static final String REDIS_HEART_BEAT_STATE = "REDIS_HEART_BEAT"; //redis心跳检测标志key
    public static volatile int FRESH_ARTICLE_ID = -1;  //最新一期的id
    public static final String ARTICLEID_LOVE_HASH = "ARTICLEID_LOVE_HASH";    //单条动态的点赞列表
    public static final String SURFACES_LIST_KEY = "surfaces";  //封面列表的list key
    public static long SURFACES_EXPIRE_TIME = 60 * 60 * 2;
    public static final String ARTICLEID_CONMENT_LIST = "ARTICLEID_COMMENT_LIST";  //单条动态的评论id顺序
    public static final String ARTICLEID_COMMENTID_HASH = "ARTICLEID_COMMENTID_HASH"; //动态的单条评论

    //返回码定义
    public static final int PARAM_ERROR = 410;   //参数错误返回码
    public static final int VIRIFYCODE_OVERCOUNT = 411;   //邮箱验证码次数超过阈值返回码
    public static final int REQUEST_SUCCESS = 200; //请求响应成功返回码
    public static final int REQUEST_FAILED = 501;   //业务逻辑错误，请求结果错误
    public static final int MYSQL_INFO_REPEAT = 502;  //数据库信息重复导致出错
    public static final int SERVICE_ERROR = 505; //服务出错返回码
    public static final int USER_INT_BLACKLIST = 888;  //用户处于黑名单中
    public static final int TOKEN_ERROR = 800;  //token解析错误
    public static final int NO_PERMISSION = 555;  //用户没有权限
    public static final int NO_DATA_IN_MYSQL = 503;  //数据库没有数据


    public enum CampusEnum{
        XIAO_YING("小营",0),
        QING_HE("清河",1),
        SHA_HE("沙河",2),
        JIAN_XIANG_QIAO("健翔桥",3);

        private String name;
        private int index;
        // 构造方法
        CampusEnum(String name,int index) {
            this.name = name;
            this.index = index;
        }
        public static boolean isCampus(int num){
            boolean res = false;
            for (CampusEnum e: CampusEnum.values()){
                if(e.index == num){
                    res = true;
                    break;
                }
            }
            return res;
        }
    }

    public static int ARTICLE_STATE = 0;  //用户发布动态默认的标志位，代表未审核
    public static int CHOOSE_ARTICLE_STATE = 1;  //管理员选择动态，更改标志位，1代表已被选择，可以发表
    public static int RED_COLOR = -1; // 管理员标红为红色，特殊
    public static String HIDE_IMGS = "";  //屏蔽之后图片列表值
    public static String HIDE_CONTENT = "XXX(已屏蔽)";  //屏蔽之后内容值
    public static String HASH_ARTICLE_PRE_KEY = "hashArticle";



    public enum ColorEnum{
        ORANGE_COLOR("橙色",0),
        YELLOW_COLOR("黄色",1),
        GREEN_COLOR("绿色",2),
        CYAN_COLOR("青色",3),
        BLUE_COLOR("蓝色",4),
        BLACK_COLOR("黑色",5);

        private String name;
        private int index;
        // 构造方法
        ColorEnum(String name,int index) {
            this.name = name;
            this.index = index;
        }
        public static boolean isColor(int num){
            boolean res = false;
            for (ColorEnum e: ColorEnum.values()){
                if(e.index == num){
                    res = true;
                    break;
                }
            }
            return res;
        }
    }

    public static void main(String[] args) {

        String[] s = "".split(",");
        System.out.println(s.length);
        //System.out.println(new File(Constant.AVATAR_URL + "1234.jpg").delete());


    }

}

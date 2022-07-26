package com.Utils;

import com.alibaba.fastjson.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Map;

/**
 * @author 13049
 * 微信授权登录，根据用户的code码换取微信小程序的openId
 */
public class GetOpenId {
    public static final String GETCODE_ERROR = "code is error!";
    public static String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=wx5e8f34f1928222bc&secret=a8cf12df1e597d23dc6c584bcdacaa9c&js_code=";
    public static String getOpenId(String code){
        String url = URL + code + "&grant_type=authorization_code";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        String openid = "";
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                openid = response.body().string();
                Map<String, String> map = (Map<String,String> )JSON.parse(openid);
                openid = map.get("openid");
            }
        } catch (IOException e) {
            return GETCODE_ERROR;
        }
        return openid;
    }
}

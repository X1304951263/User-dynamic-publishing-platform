package com.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 13049
 */
public class TokenUtil {
    private static final long EXPIRE_TIME = 1000 * 60 * 60 * 2; //token过期时间为2小时
    private static final String PRIVATE_KEY = "darling";  //密钥

    /**
     * 签名生成
     * @param
     * @return
     */
    public static String sign(String id,int auth,int signin){
        String token = "";
        Map<String,Object> header = new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        Map<String,Object> claims = new HashMap<>();
        //自定义有效载荷部分
        claims.put("openId",id);
        claims.put("auth",auth);
        claims.put("signin",signin);
        token = Jwts.builder()
                //发证人
                .setIssuer("auth")
                //Jwt头
                .setHeader(header)
                //有效载荷
                .setClaims(claims)
                //设定签发时间
                .setIssuedAt(new Date())
                //设定过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                //使用HS256算法签名，PRIVATE_KEY为签名密钥
                .signWith(SignatureAlgorithm.HS256,PRIVATE_KEY)
                .compact();
        return token;
    }

    /**
     * @param token
     * @return  用户的openId
     */
    public static String  getOpenId(String token){
        Claims claims = Jwts.parser()
                //设置 密钥
                .setSigningKey(PRIVATE_KEY)
                //设置需要解析的 token
                .parseClaimsJws(token).getBody();
        return claims.get("openId").toString();
    }

    /**
     * @param token
     * @return  用户的角色位
     */
    public static int getAuth(String token){
        Claims claims = Jwts.parser()
                //设置 密钥
                .setSigningKey(PRIVATE_KEY)
                //设置需要解析的 token
                .parseClaimsJws(token).getBody();
        return (int) claims.get("auth");
    }

    /**
     * @param token
     * @return  用户的登录渠道
     */
    public static int getSignin(String token){
        Claims claims = Jwts.parser()
                //设置 密钥
                .setSigningKey(PRIVATE_KEY)
                //设置需要解析的 token
                .parseClaimsJws(token).getBody();
        return (int) claims.get("signin");
    }

    /**
     * 验证 token信息 是否正确
     * @param token 被解析 JWT
     * @return 是否正确
     */
    public static boolean verify(String token){
        try{
            Jwts.parser()
                    //设置 密钥
                    .setSigningKey(PRIVATE_KEY)
                    //设置需要解析的 token
                    .parseClaimsJws(token).getBody();
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
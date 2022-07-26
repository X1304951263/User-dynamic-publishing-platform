package com.Utils;

public class ParamCheckUtil {

    public static String regex =  "^[a-z0-9A-Z]+$";    //正则判断账号密码是否仅由字母或数字组成
    //正则判断邮箱是否合格
    public static String email_Regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    //判断账号是否符合要求（6-14位字母或数字）
    public static boolean isAccount(String str) {
        if(str == null || str.length() < Constant.MIN_ACCOUNT_LENGTH || str.length() > Constant.MAX_ACCOUNT_LENGTH){
            return false;
        }
        return str.matches(regex);
    }

    //判断密码是否符合要求（6-14位字母或数字）
    public static boolean isWord(String str) {
        if(str == null || str.length() < Constant.MIN_WORD_LENGTH || str.length() > Constant.MAX_WORD_LENGTH){
            return false;
        }
        return str.matches(regex);
    }

    //判断邮箱是否符合要求
    public static boolean isEmail(String str) {
        if(str == null || str.equals("")){
            return false;
        }
        //正则判断邮箱是否合格
        return str.matches(email_Regex);
    }

    //判断验证码是否由6位纯数字组成
    public static boolean isCode(String str){
        if(str == null || str.equals("") || str.length() != Constant.VIRIFYCODE_LENGTH){
            return false;
        }
        return str.matches("[0-9]+");
    }

}

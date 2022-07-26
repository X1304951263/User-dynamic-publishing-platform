package com.Utils;

import org.springframework.mail.SimpleMailMessage;

public class SendVirifyCode {
    public static SimpleMailMessage sendMes(String email,String code){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //设置发件人
        simpleMailMessage.setFrom("问天羽<1304951263@qq.com>");
        //设置邮件的主题
        simpleMailMessage.setSubject("验证码");
        //设置邮件的内容
        String message = "[北信科] 您的验证码是" + code +"，5分钟内有效。如非本人操作，请忽略此邮件";
        simpleMailMessage.setText(message);
        //设置邮件的收件人
        simpleMailMessage.setTo(email);
        return simpleMailMessage;
    }

    /**
     *通过StringBuilder构建一个字符串，用random函数生成6次，生成一个6位数字验证码
     * @return      返回六位数字验证码
     */
    public static String getVerifyCode(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < Constant.VIRIFYCODE_LENGTH; i++){
            int num = (int)(Math.random()*10);
            str.append(num);
        }
        return str.toString();
    }

}

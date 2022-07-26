package com.DAO;

import com.Entity.BlackReason;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlackReasonDao {

    @Results({
            @Result(property = "openId", column = "openId"),
            @Result(property = "content", column = "content"),
            @Result(property = "img", column = "img")
    })

    //举报用户，将用户信息插入到黑名单表中以待后续具体操作
    @Insert("INSERT into black_reason(openId,content,img) values(#{0},#{1},#{2})")
    boolean informUser(String openId, String content,String img);

    //查看举报原因
    @Select("SELECT content,img FROM black_reason WHERE  openId = #{0}")
    List<BlackReason> getInformReason(String openId);

    //删除举报信息
    @Delete("DELETE from black_reason where openId = #{0}")
    boolean delInformReason(String openId);

    //获取图片路径，方便后期删除图片文件
    @Select("SELECT img FROM black_reason WHERE openId = #{0}")
    List<BlackReason> getImg(String openId);
}

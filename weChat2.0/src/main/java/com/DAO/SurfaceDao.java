package com.DAO;

import com.Entity.Surface;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SurfaceDao {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "img", column = "img"),
            @Result(property = "content", column = "content"),
            @Result(property = "timestamp", column = "timestamp")
    })

    //管理员发布动态,添加封面
    @Insert("INSERT into surface(img,content,timestamp) values(#{0},#{1},#{2})")
    boolean insertSurface(String img,String content,long timestamp);

    @Select("select max(id) from surface")
    Integer getId();

    //获取最新一期
    @Select("select id,img,content,timestamp from surface where id = #{0} limit 1")
    Surface getFreshedSurface(int id);

    //获取所有期
    @Select("select id,img,content,timestamp from surface order by id DESC")
    List<Surface> getAllSurface();
}

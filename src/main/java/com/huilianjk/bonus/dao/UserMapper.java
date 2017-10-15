package com.huilianjk.bonus.dao;

import com.huilianjk.bonus.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by space on 2017/10/15.
 */
public interface UserMapper {
    @Insert("insert into user(open_id,nickname,create_time)values(#{user.openId},#{user.nickname},now())")
    @Options(useGeneratedKeys = true, keyProperty = "user.userId")
    void insert(@Param("user") User user);

    @Select("select * from user where ")
    User getUserByOpenId(@Param("openId") String openId);
}

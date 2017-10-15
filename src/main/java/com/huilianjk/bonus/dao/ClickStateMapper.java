package com.huilianjk.bonus.dao;

import com.huilianjk.bonus.pojo.ClickState;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * Created by space on 2017/10/15.
 */
public interface ClickStateMapper {
    @Insert("insert into click_state(bonus_id,user_id,create_time)values(#{clickState.bonusId},#{clickState.userId},now())")
    void insert(@Param("clickState") ClickState clickState);
}

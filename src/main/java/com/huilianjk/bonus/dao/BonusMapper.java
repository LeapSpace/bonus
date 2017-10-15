package com.huilianjk.bonus.dao;

import com.huilianjk.bonus.pojo.Bonus;
import org.apache.ibatis.annotations.*;

/**
 * Created by space on 2017/10/15.
 */
public interface BonusMapper {
    @Insert("insert into bonus (platform,sn,owner_id,owner_name,luck_number,create_time)values" +
            "(#{bonus.platform},#{bonus.sn},#{bonus.ownerId},#{bonus.ownerName},#{bonus.luckNumber},now())")
    @Options(useGeneratedKeys = true,keyProperty = "bonus.bonusId")
    void insert(@Param("bonus") Bonus bonus);

    @Update("update bonus set current_count=current_count+1 where bonus_id=#{bonusId}")
    int addClickCount(@Param("bonusId") long bonusId);

    @Select("select * from bonus where bonus_id=#{bonusId}")
    Bonus getBonusById(@Param("bonusId") long bonusId);

    @Select("select * from bonus where platform=#{platform} and sn=#{sn}")
    Bonus getBonusByPlatformAndSn(@Param("platform") String platform, @Param("sn") String sn);
}

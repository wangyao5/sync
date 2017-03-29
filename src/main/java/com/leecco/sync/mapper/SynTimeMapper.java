package com.leecco.sync.mapper;


import com.leecco.sync.model.SynTimeModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SynTimeMapper {
    @Insert("insert into T_MCL_SynTime(FSynTime,FIntNo)values(#{synTime},#{intNo})")
    void save(SynTimeModel logModel);
    @Select("select * from T_MCL_SynTime where FIntNo=#{intNo} order by FSynTime DESC")
    SynTimeModel selectByNum(String number);
    @Update("update T_MCL_SynTime set FSynTime=#{synTime} where FIntNo=#{intNo}")
    void update(SynTimeModel logModel);
}

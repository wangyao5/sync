package com.leecco.sync.mapper;

import com.leecco.sync.model.LogModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LogMapper {

    @Insert("insert into T_MCL_Log(FSynTime,FIntNo,FStatus,FParams,FResult,FMidParams,FTotalCount,FSucessCount) values(#{synTime},#{intNo},#{status},#{params},#{result},#{midParams},#{totalCount},#{successCount})")
    void save(LogModel logModel);

    @Select("select fid,FSynTime,FParams,FResult from T_MCL_Log where FintNo=#{id} order by FSynTime DESC")
    List selectById(String id);

    @Select("select fid,FSynTime,FParams,FResult from T_MCL_Log")
    List selectAll();

    @Select("select fid,FSynTime,FParams,FResult from T_MCL_Log where FintNo=#{0} order by FSynTime DESC LIMIT #{1},#{2}")
    List selectByLimit(String id, int no1, int no2);

    @Select("select fid,FSynTime,FParams,FResult from T_MCL_Log where 1=1 and #{filter} order by FSynTime DESC")
    List selectByFilter(String filter);
}

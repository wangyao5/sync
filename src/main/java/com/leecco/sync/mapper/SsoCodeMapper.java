package com.leecco.sync.mapper;

import com.leecco.sync.model.SsoCodeModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SsoCodeMapper {
	@Select("select fid, FCode,FUrl from t_sso_code where FCode=#{code}")
	List selectByCode(String code);
	@Update("update t_sso_code set FUrl=#{url} where FCode=#{code}")
	void update(SsoCodeModel model);
	@Insert("insert into t_sso_code(FCode,FUrl)values(#{code},#{url})")
	void save(SsoCodeModel model);
	@Select("select fid, FCode,FUrl from t_sso_code")
	List selectAllCode();
	@Select("select fid, FCode,FUrl  from t_sso_code  order by fid DESC LIMIT #{0},#{1}")
	List selectByLimit(int m, int n);
}

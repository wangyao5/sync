package com.leecco.sync.mapper;

import com.leecco.sync.model.OrgModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrgMapper {
	@Insert("insert into T_ORG_ADMIN(Forg_num,Forg_name,Forg_pnum)values(#{org_num},#{org_name},#{pNum})")
	void save(OrgModel orgModel);

	@Select("select * from T_ORG_ADMIN where Forg_num=#{orgNum}")
	List<OrgModel>  selectByNum(String orgNum);

	@Select("select * from T_ORG_ADMIN")
	List<OrgModel>  selectAll();

	@Update("update T_ORG_ADMIN set Forg_name=#{org_name},Forg_pnum=#{pNum} where Forg_num=#{org_num}")
	void updateOrgModel(OrgModel orgModel);
}

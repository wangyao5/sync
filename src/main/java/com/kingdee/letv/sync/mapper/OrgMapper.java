package com.kingdee.letv.sync.mapper;

import java.util.List;

import com.kingdee.letv.sync.model.OrgModel;

public interface OrgMapper {
	public void save(OrgModel orgModel);

	public List<OrgModel>  selectByNum(String id);

	public List<OrgModel>  selectAll();
	
	public void updateOrgModel(OrgModel orgModel);
}

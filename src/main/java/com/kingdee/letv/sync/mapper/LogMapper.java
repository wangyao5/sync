package com.kingdee.letv.sync.mapper;

import java.util.List;

import com.kingdee.letv.sync.model.LogModel;

public interface LogMapper {


	public void save(LogModel logModel);
	
	public List  selectById(String id);
	
	public List  selectAll();
	
	public List  selectByLimit(String id,int no1,int no2);
	
	public List  selectByFilter(String filter);
}

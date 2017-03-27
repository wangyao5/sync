package com.kingdee.letv.sync.mapper;


import com.kingdee.letv.sync.model.SynTimeModel;

public interface SynTimeMapper {
	
	public void save(SynTimeModel logModel);
	
	public SynTimeModel selectByNum(String number);
	
	public void update(SynTimeModel logModel);
	

}

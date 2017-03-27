package com.kingdee.letv.sync.mapper;

import java.util.List;

import com.kingdee.letv.sync.model.SsoCodeModel;

public interface SsoCodeMapper {
	public List selectByCode(String code);
	public void update(SsoCodeModel model);
	public void save(SsoCodeModel model);
	public List selectAllCode();
	public List selectByLimit(int m,int n);
}

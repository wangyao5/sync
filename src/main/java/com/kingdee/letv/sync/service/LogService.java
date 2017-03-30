package com.kingdee.letv.sync.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingdee.letv.sync.mapper.LogMapper;
import com.kingdee.letv.sync.mapper.OrgMapper;
import com.kingdee.letv.sync.mapper.SsoCodeMapper;
import com.kingdee.letv.sync.mapper.SynTimeMapper;
import com.kingdee.letv.sync.model.LogModel;
import com.kingdee.letv.sync.model.OrgModel;
import com.kingdee.letv.sync.model.SsoCodeModel;
import com.kingdee.letv.sync.model.SynTimeModel;

public class LogService {
	
	public SqlSession openSession(){
		return null;
	}
	
	public void commitsession(SqlSession session){
	}
	public SsoCodeModel getSSoCodeModel(String code){
		return null;
	}
	
	public LogModel saveLogModel(LogModel model){
		return null;
	}
	public List selectAll(){
		return null;
	}
	public List  selectByFilter(String filter){
		return null;
	}
	public List  selectById(String filter){
		return null;
	}
	public SynTimeModel getSynTimeModel(String intNo){
		return null;
	}
	
	public SynTimeModel saveSynTimeModel(SynTimeModel model){
		return null;
	}
	public void saveOrgModel1(OrgModel orgModel){
	}
	
	public void saveOrgModel(SqlSession session,OrgModel orgModel){
	}
	
	public List<OrgModel> selectByNum(String num){
		return null;
	}
	public List<OrgModel> selectAllOrgModel(){
		return null;
	}
	public List<OrgModel> selectByLimit(String id,int no1,int no2){
		return null;
	}
	
	public void updateOrgModel(SqlSession session,OrgModel model){
	}
	
	public SsoCodeModel saveSsoCodeModel(SsoCodeModel model){
	   return null;	
        }
	public List selectAllCode(){
		return null;
	}
	public List selectByLimit(int m,int n){
		return null;
	}
}

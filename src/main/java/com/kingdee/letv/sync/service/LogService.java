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
	private static SqlSessionFactory sqlSessionFactory;
	private final static Logger logger=LoggerFactory.getLogger(LogService.class);
	
	static{
		try {
			String resource = "config/mybatis-config.xml";
			File file =new File(resource);
			InputStream inputStream = new FileInputStream(file);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SqlSession openSession(){
		SqlSession session=sqlSessionFactory.openSession();
		return session;
	}
	
	public void commitsession(SqlSession session){
		session.commit();
		session.close();
	}
	public SsoCodeModel getSSoCodeModel(String code){
		SqlSession session=sqlSessionFactory.openSession();
		SsoCodeMapper mapper=session.getMapper(SsoCodeMapper.class);
		List<SsoCodeModel> l=mapper.selectByCode(code);
		if(l!=null && l.size()>0){
			return l.get(0);
		}
		return null;
	}
	
	
	/**
	 * @param model
	 * @return
	 */
	public LogModel saveLogModel(LogModel model){
		SqlSession session=sqlSessionFactory.openSession();
		LogMapper mapper=session.getMapper(LogMapper.class);
		mapper.save(model);
		session.commit();
		session.close();
		return model;
	}
	public List selectAll(){
		SqlSession session=sqlSessionFactory.openSession();
		LogMapper mapper=session.getMapper(LogMapper.class);
		List list=mapper.selectAll();
		session.close();
		return list;
	}
	public List  selectByFilter(String filter){
		SqlSession session=sqlSessionFactory.openSession();
		LogMapper mapper=session.getMapper(LogMapper.class);
		List list= mapper.selectByFilter(filter);
		session.close();
		return list;
	}
	public List  selectById(String filter){
		SqlSession session=sqlSessionFactory.openSession();
		LogMapper mapper=session.getMapper(LogMapper.class);
	
		 List list= mapper.selectById(filter);
		
		session.close();
		return list;
	}
	public SynTimeModel getSynTimeModel(String intNo){
		SqlSession session=sqlSessionFactory.openSession();
		SynTimeMapper mapper=session.getMapper(SynTimeMapper.class);
		SynTimeModel synTimeModel=mapper.selectByNum(intNo);
		if(synTimeModel==null){
			synTimeModel=new SynTimeModel();
			synTimeModel.setSynTime(new Timestamp(System.currentTimeMillis()));
			synTimeModel.setIntNo(intNo);
		}
		session.commit();
		session.close();
		return synTimeModel;
	}
	
	public SynTimeModel saveSynTimeModel(SynTimeModel model){
		SqlSession session=sqlSessionFactory.openSession();
		SynTimeMapper mapper=session.getMapper(SynTimeMapper.class);
		if(model.getId()<=0){
			mapper.save(model);
		}else{
			mapper.update(model);
		}
		session.commit();
		session.close();
		return model;
	}
	public void saveOrgModel1(OrgModel orgModel){
		SqlSession session=sqlSessionFactory.openSession();
		OrgMapper mapper=session.getMapper(OrgMapper.class);
		if(orgModel.getId()<=0){
			mapper.save(orgModel);
		}else{
			mapper.updateOrgModel(orgModel);
		}
		session.commit();
		session.close();
	}
	
	public void saveOrgModel(SqlSession session,OrgModel orgModel){
		OrgMapper mapper=session.getMapper(OrgMapper.class);
		if(orgModel.getId()<=0){
			mapper.save(orgModel);
		}else{
			mapper.updateOrgModel(orgModel);
		}
	}
	
	public List<OrgModel> selectByNum(String num){
		SqlSession session=sqlSessionFactory.openSession();
		OrgMapper mapper=session.getMapper(OrgMapper.class);
		
		List<OrgModel> list= mapper.selectByNum(num);
		session.close();
		return list;
	}
	public List<OrgModel> selectAllOrgModel(){
		SqlSession session=sqlSessionFactory.openSession();
		OrgMapper mapper=session.getMapper(OrgMapper.class);		
		List<OrgModel> list= mapper.selectAll();
		session.close();
		return list;
	}
	public List<OrgModel> selectByLimit(String id,int no1,int no2){
		SqlSession session=sqlSessionFactory.openSession();
		LogMapper mapper=session.getMapper(LogMapper.class);		
		List<OrgModel> list=mapper.selectByLimit(id,no1, no2);
		session.close();
		return list;
	}
//	public void updateOrgModel(OrgModel model){
//		SqlSession session=sqlSessionFactory.openSession();
//		OrgMapper mapper=session.getMapper(OrgMapper.class);
//		mapper.updateOrgModel(model);
//		session.commit();
//		session.close();
//	}
	
	public void updateOrgModel(SqlSession session,OrgModel model){
//		SqlSession session=sqlSessionFactory.openSession();
		OrgMapper mapper=session.getMapper(OrgMapper.class);
		mapper.updateOrgModel(model);
	}
	
	public SsoCodeModel saveSsoCodeModel(SsoCodeModel model){
		SqlSession session=sqlSessionFactory.openSession();
		SsoCodeMapper mapper=session.getMapper(SsoCodeMapper.class);
		if(model.getId()==0){
			mapper.save(model);
		}else{
			mapper.update(model);
		}
		session.commit();
		return model;
	}
	public List selectAllCode(){
		SqlSession session=sqlSessionFactory.openSession();
		SsoCodeMapper mapper=session.getMapper(SsoCodeMapper.class);
		List list=mapper.selectAllCode();
		session.close();
		return list;
	}
	public List selectByLimit(int m,int n){
		SqlSession session=sqlSessionFactory.openSession();
		SsoCodeMapper mapper=session.getMapper(SsoCodeMapper.class);		
		List list=mapper.selectByLimit(m, n);
		session.close();
		return list;
	}
}

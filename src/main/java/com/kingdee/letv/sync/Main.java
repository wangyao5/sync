package com.kingdee.letv.sync;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.kingdee.letv.sync.model.SynTimeModel;
import com.kingdee.letv.sync.service.LogService;
import com.kingdee.letv.sync.util.ClassLoaderUtil;
import com.kingdee.letv.sync.util.ConfigUtil;
import com.kingdee.letv.sync.util.OrgSyncUtil;
import com.kingdee.letv.sync.util.PersonSyncUtil;

public class Main {

	public  void test(String[] args) {
        ClassLoaderUtil.loadJarPath("lib");
        ConfigUtil configUtil=new ConfigUtil("config/config.properties");
       // ConfigUtil configUtil=new ConfigUtil(System.getProperty("config.Path"));
        // 初始化log4j
//        try { 
//            Class<?> clz = Class.forName("org.apache.log4j.PropertyConfigurator");
//            Method method = clz.getMethod("configure", String.class);
//            method.invoke(clz, "config/log4j.properties");
//        } catch (Throwable ignore) {
//        } 

/*        try {
            Class<?> clz = Class.forName("org.apache.logging.log4j.core.config.Configurator");
            Method method = clz.getMethod("initialize", String.class, String.class);
            method.invoke(clz, null, "config/log4j2.xml");
        } catch (Throwable ignore) {

        }*/
        

		LogService logService=new LogService();
		SynTimeModel orgmodel =	logService.getSynTimeModel("ORG");
		SynTimeModel personmodel =	logService.getSynTimeModel("PERSON");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastOrgTime ="";
		String lastPerTime ="";

		if(orgmodel!=null && orgmodel.getSynTime()!=null){
			lastOrgTime =sdf.format(orgmodel.getSynTime());
			lastPerTime =sdf.format(personmodel.getSynTime());
		}else{
			lastOrgTime = "2013-10-01 00:00:00";
			lastPerTime = "2013-10-01 00:00:00";
		}
	//	OrgSyncUtil.syncOrg(lastTime);
	
        
        try {//先同步部门
        	OrgSyncUtil OrgSyncUtil =new OrgSyncUtil();
        	OrgSyncUtil.startOrgSync(lastOrgTime);
			orgmodel.setSynTime(new Timestamp(System.currentTimeMillis()));
			logService.saveSynTimeModel(orgmodel);
		} catch (Exception e) {
			System.out.println("同步部门报错："+e.getMessage());
			e.printStackTrace();
			// TODO: handle exception
		}finally{
		//	orgmodel.setSynTime(new Timestamp(System.currentTimeMillis()));
			//logService.saveSynTimeModel(orgmodel);
		}
        
        
        try {//再同步人员
        	PersonSyncUtil OrgSyncUtil =new PersonSyncUtil();
        	OrgSyncUtil.startPersonSync(lastPerTime);
			personmodel.setSynTime(new Timestamp(System.currentTimeMillis()));
			logService.saveSynTimeModel(personmodel);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
		//	personmodel.setSynTime(new Timestamp(System.currentTimeMillis()));
		//	logService.saveSynTimeModel(personmodel);
		}
        
        // 启动
	}
	
}

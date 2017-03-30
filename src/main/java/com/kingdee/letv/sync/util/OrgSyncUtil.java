package com.kingdee.letv.sync.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.letv.sync.been.DeptDTO;
import com.kingdee.letv.sync.been.DeptOrgInfo;
import com.kingdee.letv.sync.model.OrgModel;
import com.kingdee.letv.sync.service.LogService;

public class OrgSyncUtil {
	public static List<String> del = new ArrayList<String>();
	public static Map<String,OrgModel> cache = new HashMap<String, OrgModel>();
	static Map<String,JSONObject> allOrgs = null;

    private final static Logger logger=Logger.getLogger(PersonSyncUtil.class);
	public static void test(String[] args) {
		//初始化
		allOrgs = getAllOrg();
		syncOrg("2016-09-05 00:00:00");
		System.out.println(LetvUtil.KEY);
	}
	

	
	public void startOrgSync(String lastTime){

		//初始化
		allOrgs = getAllOrg();
		syncOrg(lastTime);
		System.out.println(LetvUtil.KEY);
		delXtOrg();
	
	}
	public static String syncOrg(String lastTime) {
		String result = "";
		String json = "";
		try {
			json = setDepartment(lastTime);
			postDepartment(json);
			synDelOrg(lastTime);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return result;
	}
	
	public static void delXtOrg(){
		List<String> listorg=new ArrayList<String>();
		String[] delOrg = new String[10];
		if(allOrgs==null){
			return ;
		}
		if(allOrgs.size()>200){
			logger.warn("del dept max 200............."+allOrgs.size());
			return ;
		}
		for(String org:allOrgs.keySet()){
			listorg.add(org);
			if(listorg.size()==10){
				DeptDTO depts = new DeptDTO();
				depts.setEid(LetvUtil.KEY);
				depts = new DeptDTO();
				depts.setEid(LetvUtil.KEY);
				depts.setDepartments(listorg.toArray(new String [0]));
				String jsons =JSON.toJSONString(depts);
				logger.info("删除部门..."+jsons);
				deleteDepartment(jsons);
				listorg.clear();//10个部门一删除
			}

		}
		
		if(listorg.size()>0){
			DeptDTO depts = new DeptDTO();
			depts.setEid(LetvUtil.KEY);
			depts = new DeptDTO();
			depts.setEid(LetvUtil.KEY);
			depts.setDepartments(listorg.toArray(new String [0]));
			String jsons =JSON.toJSONString(depts);
			logger.info("删除部门..."+jsons);
			deleteDepartment(jsons);
			listorg.clear();//10个部门一删除
		}

	}
	
	//同步禁用组织
	private static void synDelOrg(String lastTime){
		//循环同步禁用组织
		//记录条数
		//根据日期判断是否是第一次同步：第一次同步不同步禁用组织
		if(lastTime!=null && lastTime.length()>4){
			int m = 0;
			String[] delOrg = new String[500];
			String[] de = null;
			if(del!=null &&del.size()>0){
				DeptDTO depts = new DeptDTO();
				depts.setEid(LetvUtil.KEY);
				depts = new DeptDTO();
				for (int j = 0; j < del.size(); j++) {
					if(del.get(j)==null)continue;
					String number = del.get(j).toString();
					//如果禁用组织在系统中不存在，不进行同步
					if(!cache.containsKey(number))continue;
					if(m>=500){
						depts.setEid(LetvUtil.KEY);
						depts.setDepartments(delOrg);
						String jsons =JSON.toJSONString(depts);
						deleteDepartment(jsons);
						m=0;
						delOrg = new String[500];
					}
					if(cache.get(number)==null)continue;
					OrgModel orgModel =cache.get(number);
					delOrg[m] =orgModel.getOrg_name();
					m++;
				}
				de = new String[m];
				de = Arrays.copyOfRange(delOrg, 0, m);
				depts.setDepartments(de);
				depts.setEid(LetvUtil.KEY);
				String jo = JSON.toJSONString(depts);
				deleteDepartment(jo);
			}
		}
	}
	private static String postDepartment(String json){
		String result = "";
		String  url = LetvUtil.host   + "openaccess/input/dept/add";
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
		nvps.add(new BasicNameValuePair("nonce", String.valueOf(cal.getTimeInMillis())));  
		nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
		System.out.println("KEY值："+LetvUtil.KEY);
		System.out.println("(fasongshuju)FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF：");
		System.out.println("发送的数据："+json);
		JSONObject jsonDep = JSON.parseObject(json);
		JSONArray jsonA =(JSONArray) jsonDep.get("departments");
		if(jsonA==null || jsonA.size()==0)return result;
		nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json)));
		System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
		result= LetvUtil.post(url,nvps);
		System.out.println("(fasongchengg)SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS:"+result);
	//	LetvUtil.writeLog(json,result,"ORG");
		return result;
	}
	private static String deleteDepartment(String json){
		String result = "";
		String  url = LetvUtil.host   + "openaccess/input/dept/delete";
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
		nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
		System.out.println("(jinyong shujufasong)JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ：");
		JSONObject jsonDep = JSON.parseObject(json);
		JSONArray jsonA =(JSONArray) jsonDep.get("departments");
		if(jsonA==null || jsonA.size()==0)return result;
		nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json)));
		System.out.println("KKKKKKKKKKKKKKKKKKKKKJJJJJJJJJJJJJJJJJJJJJJJ");
		result= LetvUtil.post(url,nvps);
		System.out.println("JJJJJJJJJJJJJRESULTJJJJJJJJJ"+result);
		//LetvUtil.writeLog(json,result,"ORG");
		return result;
	}
	
	private static String getAllDeptment(String lastTime,String starTime,String endTime,String time,String sign, String orgUrl,JSONArray deptjson)throws Exception{
		if(deptjson==null||deptjson.size()==0){
			
		}
			URL postUrlInvoke = new URL(orgUrl);
			HttpURLConnection connectionInvoke = (HttpURLConnection)postUrlInvoke.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInvoke.getInputStream(),"UTF-8")); 
	

		return "";
	}
	
	private static String setDepartment(String lastTime) throws Exception{
		String POST_URL_Invoke_Org = LetvUtil.POST_URL_Invoke_Org;
		DeptDTO depts = new DeptDTO();
		depts.setEid(LetvUtil.KEY);
		String start = lastTime;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String end = sdf.format(new Date());
		List<String> parm=new ArrayList<String>();
		parm.add("ps");
		parm.add("endtime");
		parm.add("site");
		parm.add("time");
		parm.add("p");
		System.out.println(parm.toString());
		Collections.sort(parm);
		System.out.println(parm.toString());
		Map<String, String> orgNum = new HashMap<String, String> ();
		Map<String, DeptOrgInfo> org = new HashMap<String, DeptOrgInfo>();
		int page=1;
		while (true){
			System.out.println("page:"+page);
			String time =(System.currentTimeMillis()+"").substring(0,10);
			String sign = LetvUtil.getMD5("endtime="+end+"&p="+page+"&site=app&starttime="+start+"&time="+time+"8956dmqwqddsxwfvt977");
			String postUrl= POST_URL_Invoke_Org+"p="+page+"&time="+time+"&endtime="+URLEncoder.encode(end, "utf-8")+"&starttime="+URLEncoder.encode(start, "utf-8")+"&sign="+sign;
			URL postUrlInvoke = new URL(postUrl);
			System.out.println(postUrl);
			HttpURLConnection connectionInvoke = (HttpURLConnection)postUrlInvoke.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInvoke.getInputStream(),"UTF-8")); 
			String line ="";
			JSONArray json =null;
			if ((line = reader.readLine()) != null) { //
				System.out.println("line:"+line);
				JSONObject jsonDep = JSON.parseObject(line);
				json=(JSONArray) jsonDep.get("objects");
				if(json==null){
					break;
				}
				System.out.println("获取到的组织信息 size:"+json.size());
				logger.info("获取到的组织信息:"+JSON.toJSONString(json));
				System.out.println(JSON.toJSONString(json));
				ListIterator node = json.listIterator();
				while(node.hasNext()){
					JSONObject dep =(JSONObject) node.next();
					DeptOrgInfo info = new DeptOrgInfo();
					if("10000073".equals(dep.getString("org_num"))||"LE00310000074".equals(dep.getString("org_num"))){
						System.out.println(dep.getString("org_num"));
					}
					info.setId(dep.getString("org_num"));
					info.setName(dep.getString("org_name"));
					info.setNumber(dep.getString("org_num"));
					info.setParentNumber(dep.getString("pnum"));
					info.setEffect(dep.getIntValue("effect"));
					info.setLevel(dep.getIntValue("level"));
					orgNum.put(dep.getString("org_num"), dep.getString("pnum"));
					org.put(dep.getString("org_num"), info);
					if(dep.getIntValue("effect")!=1){
						del.add(dep.getString("org_num"));
					}
				}
			} 
			reader.close(); 
			connectionInvoke.disconnect(); 
			if(json==null||json.size()<100){
				break;
			}
			page++;
		}
		

		String[] strOrg = new String[500];
		int i = 0;
		LogService service = new LogService();
		List<OrgModel> orgList = service.selectAllOrgModel();
		SqlSession session=service.openSession();
		for (int j = 0; orgList!=null && j < orgList.size(); j++) {
			OrgModel orgModel =orgList.get(j);
			cache.put(orgModel.getOrg_num(), orgModel);
		}
		String[] de=null; 
		if(org!=null && org.size()>0){
			depts = new DeptDTO();
			depts.setEid(LetvUtil.KEY);
			Set set = org.entrySet();
			Iterator iter = set.iterator();
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry)iter.next();
				String orgNum_key =  entry.getKey()==null ? "" : entry.getKey().toString() ;
				if(i>=500){
					depts.setEid(LetvUtil.KEY);
					depts.setDepartments(strOrg);
					String json =JSON.toJSONString(depts);
					postDepartment(json);
					i=0;
					strOrg = new String[500];
				}
				DeptOrgInfo orgInfo = (DeptOrgInfo)org.get(orgNum_key);
				String depStr = (orgInfo).getName();
				if(depStr.contains("电商发行部")){
					System.out.println(depStr);
				}
				String partParentNum=orgNum_key;
				depStr=getParentName(orgNum,org,partParentNum,depStr);

				//排除不需要同步的组织
				if(depStr==null){
					//|| depStr.contains("霍尔果斯乐嗨文化传媒有限公司") || depStr.contains("Le Technology Inc") || depStr.contains("北京网酒网电子商务有限公司")
					//|| depStr.contains("乐视影业") || depStr.contains("采购业务部")
					continue;
				}
				if(depStr.contains("电商发行部")){
					System.out.println(depStr);
				}
//				if(depStr!=null && !depStr.contains("乐视网信息技术（北京）股份有限公司") && !depStr.contains("乐视控股（北京）有限公司\\乐视控股（北京）有限公司\\战略管理部")){
//					continue;
//				}
				System.out.println((allOrgs.get(depStr)!=null) +":"+depStr);
				if(allOrgs.get(depStr)==null){
					System.out.println("不存在这个而部门"); 
					if(!del.contains(orgNum_key)){
						System.out.println("...");
					//	del.remove(orgNum_key);//表明已经删除
					}
				}else {
					allOrgs.remove(depStr);
				}
				
				//禁用组织单独同步
				if(allOrgs.get(depStr)==null&&(del==null || !del.contains(orgNum_key))&&(orgInfo.getLevel()==1 || orgInfo.getLevel()==2 || orgInfo.getLevel()==3 ||  orgInfo.getLevel()==4||  orgInfo.getLevel()==5||  orgInfo.getLevel()==6||  orgInfo.getLevel()==7)){
					strOrg[i] = depStr;

					i++;
				}
				if(cache==null ||cache.size()==0 || !cache.containsKey(orgNum_key)){
					OrgModel orgModel = new OrgModel();
					orgModel.setOrg_name(depStr);
					orgModel.setOrg_num(orgNum_key);
					orgModel.setpNum(orgInfo.getParentNumber());
					service.saveOrgModel(session,orgModel);
				}else{
					OrgModel orgModel = cache.get(orgNum_key);
					orgModel.setOrg_name(depStr);
					orgModel.setpNum(orgInfo.getParentNumber());
					service.saveOrgModel(session,orgModel);
				}
			}
			service.commitsession(session);
		}
		de = new String[i];
		de = Arrays.copyOfRange(strOrg, 0, i);

		depts.setDepartments(de);
		String jo = JSON.toJSONString(depts);
		return jo;
	}
	private static String getParentName(Map orgNum,Map org,String number,String orgLongName){
		if(orgNum.get(number)!=null && !orgNum.get(number).equals("0")&&!orgNum.get(number).equals("")){
			if(org.get(orgNum.get(number).toString())==null){
				return orgLongName;
			}
			if(orgLongName!=null && orgLongName.length()>0){
				orgLongName = ((DeptOrgInfo)org.get(orgNum.get(number).toString())).getName()+"\\"+orgLongName;
			}else{
				orgLongName = ((DeptOrgInfo)org.get(orgNum.get(number).toString())).getName();
			}
			orgLongName = getParentName(orgNum,org,orgNum.get(number).toString(),orgLongName);
		}
		return orgLongName;
	}
	
	private static Map<String,JSONObject> getAllOrg(){
		Map<String,JSONObject> orgs = new HashMap<String,JSONObject>();
		DeptDTO dat = new DeptDTO();
		dat.setEid(LetvUtil.KEY);
		String  urlOrg = LetvUtil.host  + "openaccess/input/dept/getall";
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("eid",LetvUtil.KEY));
		nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(JSON.toJSONString(dat))));
		String reponse = LetvUtil.post(urlOrg,nvps);
		if(reponse!=null){
			JSONObject jsonPerson = JSON.parseObject(reponse);
			JSONArray json =(JSONArray) jsonPerson.get("data");
			ListIterator node = json.listIterator();
			while(node.hasNext()){
				JSONObject person =(JSONObject) node.next();
				if(person.get("name")!=null && person.getString("department")!=null){
					orgs.put(person.get("department").toString(),person);
				}
			}
/*			if(json!=null&&json.size()<1000){
				
			}*/
		}
		return orgs;
	}
}

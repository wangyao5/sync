package com.kingdee.letv.sync.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.letv.sync.been.DeptDTO;
import com.kingdee.letv.sync.been.Person;
import com.kingdee.letv.sync.been.PersonAllInfo;
import com.kingdee.letv.sync.been.PersonData2DTO;
import com.kingdee.letv.sync.model.OrgModel;
import com.kingdee.letv.sync.service.LogService;

public class PersonSyncUtil {
//	static Map<String,String> allOrgs = null;
	static Map<String,OrgModel> syncAdminMap =null;
	static Map<String,Person> allPerson =new HashMap<String, Person>();
	static Map<String,String> noSyncPersonPhone = new HashMap<String,String>();
	static int number =0;
    private final static Logger logger=Logger.getLogger(PersonSyncUtil.class);
    static int addNum=0;
    static int updateNum=0;
    static int updateStatus=0;
    static int updateDept=0;
    private final static int HTTP_MAX_RETRY_COUNT=3;
    

    
    public void startPersonSync(String lastTime){
		try {
			long start=System.currentTimeMillis();
			logger.warn("start syn..");
			syncAdminMap = getAllSyncOrgs();
			getAllPerson(0,1000);
			String 			json = setPerson(lastTime);
			postPerson(json);//新增
			System.out.println("add:"+json);
			System.out.println("addNum:"+addNum);
			System.out.println("updateNum:"+updateNum);
			System.out.println("updateStatus:"+updateStatus);
			System.out.println("updateDept:"+updateDept);
			System.out.println("此次耗时："+(System.currentTimeMillis()-start)+"ms");

		} catch (Exception e) {
			logger.warn(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
	public static void main(String[] args) {
		try {
			long start=System.currentTimeMillis();
			logger.warn("start syn..");
			syncAdminMap = getAllSyncOrgs();
			getAllPerson(0,1000);
			String 			json = setPerson("2016-09-05 00:00:00");
			postPerson(json);//新增
			System.out.println("add:"+json);
			System.out.println("addNum:"+addNum);
			System.out.println("updateNum:"+updateNum);
			System.out.println("updateStatus:"+updateStatus);
			System.out.println("updateDept:"+updateDept);
			System.out.println("此次耗时："+(System.currentTimeMillis()-start)+"ms");

		} catch (Exception e) {
			logger.warn(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//syncPerson("2013-11-11");
	}
	public static String syncPerson(String lastTime) {
		System.out.println("开始人员同步");
		//初始化
		//allOrgs = getAllOrg();
		syncAdminMap = getAllSyncOrgs();
		getAllPerson(0,1000);
		getNoPhonePerson();
		System.out.println("全局变量设置完毕");
		String result = "";
		String json = "";
		try {
			json = setPerson(lastTime);
			System.out.println("发送人员数据"+json);
			postPerson(json);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
		logger.warn("同步完毕，总人数："+number);
		System.out.println("同步完毕，总人数："+number);
		return result;
	}
	//以下人员不同步手机号
	private static void getNoPhonePerson(){
		noSyncPersonPhone.put("yinliang@letv.com","尹亮");
		noSyncPersonPhone.put("liangjun@letv.com","梁军");
		noSyncPersonPhone.put("zhangzhiwei@letv.com","张志伟");
		noSyncPersonPhone.put("yuanbin@letv.com","袁斌");
		noSyncPersonPhone.put("ablikim.ablimit@letv.com","阿不力克木·阿不力米提");
		noSyncPersonPhone.put("dongzhj@letv.com","董志建");
		noSyncPersonPhone.put("hefengyun@letv.com","何凤云");
		noSyncPersonPhone.put("jiangxiaolin@letv.com","蒋晓琳");
		noSyncPersonPhone.put("zhangminhui@letv.com","张旻翚");
		noSyncPersonPhone.put("yangyongqiang@letv.com","杨永强");
		noSyncPersonPhone.put("liuhong@letv.com","刘弘");
		noSyncPersonPhone.put("haofang@letv.com","郝舫");
		noSyncPersonPhone.put("jym@letv.com","贾跃民");
		noSyncPersonPhone.put("liumiao1@letv.com","刘淼");
		noSyncPersonPhone.put("yinni@letv.com","殷妮");
		noSyncPersonPhone.put("wuyazhou@letv.com","吴亚洲");
		noSyncPersonPhone.put("qiangwei@letv.com","强炜");
		noSyncPersonPhone.put("chengshisheng@letv.com","程时盛");
		noSyncPersonPhone.put("shenwei3@letv.com","沈威");
		noSyncPersonPhone.put("leizhenjian@letv.com","雷振剑");
		noSyncPersonPhone.put("jiangnan1@letv.com","姜楠");
		noSyncPersonPhone.put("qiuzhiwei@letv.com","邱志伟");
		noSyncPersonPhone.put("jinhang@letv.com","金航");
		noSyncPersonPhone.put("yangguang6@letv.com","杨光");
		noSyncPersonPhone.put("liuwendi@letv.com","刘文娣");
		noSyncPersonPhone.put("liujianhong@letv.com","刘建宏");
		noSyncPersonPhone.put("yuhang@letv.com","于航");
		noSyncPersonPhone.put("zhanting@letv.com","詹挺");
		noSyncPersonPhone.put("lidalong@letv.com","李大龙");
		noSyncPersonPhone.put("miaozhuang@letv.com","苗壮");
		noSyncPersonPhone.put("zhanglei5@letv.com","张磊");
		noSyncPersonPhone.put("zhouxun1@letv.com","周迅");
		noSyncPersonPhone.put("raohong@letv.com","饶宏");
		noSyncPersonPhone.put("tinmok@letv.com","莫翠天");
		noSyncPersonPhone.put("tanshu@letv.com","谭姝");
		noSyncPersonPhone.put("luolinzhi@letv.com","罗林志");
		noSyncPersonPhone.put("wj.Ray@letv.cn","李锐");
		noSyncPersonPhone.put("kechunlin@letv.com","柯春林");
		noSyncPersonPhone.put("hejinsong@letv.com","贺劲松");
		noSyncPersonPhone.put("penggang@letv.com","彭钢");
		noSyncPersonPhone.put("liuchaohua@letv.com","刘超华");
		noSyncPersonPhone.put("wangzijun@letv.com","王子君");
		noSyncPersonPhone.put("gaojingshen@letv.com","高景深");
		noSyncPersonPhone.put("yanglj@letv.com","杨丽杰");
		noSyncPersonPhone.put("cuizhanliang@letv.com","崔战良");
		noSyncPersonPhone.put("hongfeng@letv.com","洪峰");
		noSyncPersonPhone.put("zhaoyicheng@letv.com","赵一成");
		noSyncPersonPhone.put("wangqiang5@letv.com","王强");
		noSyncPersonPhone.put("yuchao1@letv.com","于超");
		noSyncPersonPhone.put("jiangdongge@letv.com", "姜东阁  ");

	}
	private static Map<String,OrgModel> getAllSyncOrgs(){
		LogService service = new LogService();
		List<OrgModel> orgList = service.selectAllOrgModel();
		Map<String,OrgModel> cache = new HashMap<String, OrgModel>();
		for (int j = 0; orgList!=null && j < orgList.size(); j++) {
			OrgModel orgModel =orgList.get(j);
			cache.put(orgModel.getOrg_num(), orgModel);
		}
		return cache;
	}
	private static String postPerson(String json){
		String result = "";
		String  url = LetvUtil.host  + "/openaccess/input/person/add";
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));  
			nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
			System.out.println("发送人员数据："+json);
			JSONObject jsonDep = JSON.parseObject(json);
			JSONArray jsonA =(JSONArray) jsonDep.get("persons");
			if(jsonA==null || jsonA.size()==0)return result;
			nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json)));  
			result = LetvUtil.post(url,nvps);
			System.out.println("同步人员结果："+result);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	//	LetvUtil.writeLog(json,result,"PERSON");
		return result;
	}
	private static void getAllPerson(int begin,int count){
		String  url = LetvUtil.host  + "/openaccess/input/person/getall";
		String result = "";
		PersonAllInfo info = new PersonAllInfo();
		info.setEid(LetvUtil.KEY);
		info.setBegin(begin);
		info.setCount(count);
		String json = JSON.toJSONString(info);
		JSONArray array = new JSONArray();
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));  
			nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
			nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json))); 
			result = LetvUtil.post(url,nvps);
			if(result!=null && result.length()>0){
				JSONObject jsonobj = JSON.parseObject(result);
				array =jsonobj.getJSONArray("data");
				for(int i = 0;i<array.size();i++){
					JSONObject obj  = array.getJSONObject(i);
					Person person = new Person();
					person.setEmail(obj.getString("email"));
					person.setName(obj.getString("name"));
					person.setOpenId(obj.getString("openId"));
					person.setJobTitle(obj.getString("jobTitle"));
					person.setDepartment(obj.getString("department"));
					person.setStatus(obj.getString("status"));
					String phone=obj.getString("phone");
					person.setPhone(phone);
					if(phone!=null){
//						if(phone.equals("liting3")){
//							System.out.println("通过手机号放入liting3");
//						}
						allPerson.put(phone, person);
					}					
				}
				if(array.size()<1000){
					return ;
				}
				if(array.size()>=1000){//循环迭代获取人员信息
					begin = begin+1000;
					count = 1000;
					getAllPerson(begin,count);
				}
			}
		} catch (Exception e) {
			logger.warn("getAllPerson:"+e.getMessage());
			logger.warn("获取人员数据异常，同步即将退出。。。");
			e.printStackTrace();
			System.exit(0);
		}
	}
	//更新人员状态
	private static void updatePersonStatus(String json){
		String result = "";
		String  url = LetvUtil.host  + "/openaccess/input/person/updateStatus";
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));  
			nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
			System.out.println("更新人员状态："+json);
			JSONObject jsonDep = JSON.parseObject(json);
			JSONArray jsonA =(JSONArray) jsonDep.get("persons");
			if(jsonA==null || jsonA.size()==0)return;
			nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json)));  
			result = LetvUtil.post(url,nvps);
			System.out.println("更新人员状态完成："+result);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	//	LetvUtil.writeLog(json,result,"PERSON");
	}
	//更新人员组织
	private static void updatePersonOrg(String json){
		String result = "";
		String  url = LetvUtil.host  + "/openaccess/input/person/updateDept";
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));  
			nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
			System.out.println("更新人员组织："+json);
			JSONObject jsonDep = JSON.parseObject(json);
			JSONArray jsonA =(JSONArray) jsonDep.get("persons");
			if(jsonA==null || jsonA.size()==0)return;
			nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json)));  
			result = LetvUtil.post(url,nvps);
			System.out.println("更新人员组织完成："+result);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	//	LetvUtil.writeLog(json,result,"PERSON");
	}
	//更新人员
	private static void updatePersonDatas(String json){
		String result = "";
		String  url = LetvUtil.host  + "/openaccess/input/person/updateInfo";
		try {
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();  
			nvps.add(new BasicNameValuePair("nonce", String.valueOf(new Date().getTime())));  
			nvps.add(new BasicNameValuePair("eid", LetvUtil.KEY));  
			System.out.println("更新人员基本信息："+json);
			JSONObject jsonDep = JSON.parseObject(json);
			JSONArray jsonA =(JSONArray) jsonDep.get("persons");
			if(jsonA==null || jsonA.size()==0)return;
			nvps.add(new BasicNameValuePair("data", LetvUtil.enyte(json)));  
			result = LetvUtil.post(url,nvps);
			System.out.println("更新人员基本信息完成："+result);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn(e.getMessage());
		}
		//LetvUtil.writeLog(json,result,"PERSON");
	}
	private static String setPerson(String lastTime) throws Exception{
		String POST_URL_Invoke_User = LetvUtil.POST_URL_Invoke_USER;
		PersonData2DTO personDto = new PersonData2DTO();
		personDto.setEid(LetvUtil.KEY);
		List<Person> listPerson = new ArrayList<Person>();
		//更新人员基本信息
		List<Person> updatePerson = new ArrayList<Person>();
		List<Person> updatePersonOrg = new ArrayList<Person>();
		List<Person> updatePersonStatus = new ArrayList<Person>();
		PersonData2DTO	uppersonStatusDto = new PersonData2DTO();
		PersonData2DTO	uppersonOrgDto = new PersonData2DTO();
		PersonData2DTO	uppersonDto = new PersonData2DTO();
		String start =lastTime;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String end = sdf.format(new Date());
		int page=1;
		int retryCount=0;
		while (true){
		System.out.println("page:"+page);
		String time =(System.currentTimeMillis()+"").substring(0,10);
		System.out.println("time:"+time);
		String sign = LetvUtil.getMD5("endtime="+end+"&p="+page+"&site=app&starttime="+start+"&time="+time+"8956dmqwqddsxwfvt977");
		System.out.println("sign:"+sign);
		String postUrl= POST_URL_Invoke_User+"p="+page+"&time="+time+"&endtime="+URLEncoder.encode(end, "utf-8")
		+"&starttime="+URLEncoder.encode(start, "utf-8")+"&sign="+sign;		
		System.out.println("postUrl:"+postUrl);
		URL postUrlInvoke = new URL(postUrl);
		BufferedReader	 reader=null;
		HttpURLConnection connectionInvoke=null;
		try {
		connectionInvoke= (HttpURLConnection)postUrlInvoke.openConnection();
		connectionInvoke.setConnectTimeout(2000*10);
    	 reader = new BufferedReader(new InputStreamReader(connectionInvoke.getInputStream(),"UTF-8")); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.warn(e.getMessage());
			if(retryCount>HTTP_MAX_RETRY_COUNT){
				logger.warn("http retry :"+HTTP_MAX_RETRY_COUNT+":"+e.getMessage());	
				logger.warn("同步即将退出...");	
				System.exit(0);
			}
			retryCount++;
			continue;
			// TODO: handle exception
		}
		String line ="";
		JSONArray json =null;
		if ((line = reader.readLine()) != null) { //
			System.out.println("PerspnJson:"+line);
			JSONObject jsonDep = JSON.parseObject(line);
			json=(JSONArray) jsonDep.get("objects");
			System.out.println("获取的的人员格式:"+JSON.toJSONString(jsonDep));
			ListIterator node = json.listIterator();
			while(node.hasNext()){
				personDto.setEid(LetvUtil.KEY);
				if(listPerson!=null && listPerson.size()==10){
					personDto.setPersons(listPerson);
					String jo = JSON.toJSONString(personDto);
					postPerson(jo);
					listPerson.clear();
					personDto = new PersonData2DTO();
					personDto.setEid(LetvUtil.KEY);
				}
				JSONObject personObject =(JSONObject) node.next();
				logger.info("人员信息："+personObject.toString());
				Person info = new Person();
				String org = getOrgDep(personObject.getString("org_num"));

//				if(org!=null && !org.contains("乐视网信息技术（北京）股份有限公司\\乐视网信息技术（北京）股份有限公司\\总裁办\\集团信息化部") ){
//					continue;
//				}
				if(org==null){// || org.contains("霍尔果斯乐嗨文化传媒有限公司") || org.contains("Le Technology Inc") || org.contains("北京网酒网电子商务有限公司")
					//|| org.contains("乐视影业") || org.contains("采购业务部")
					continue;
				}
				if(org.contains("技术A组")){
					System.out.println(org);
				}
//				System.out.println();
//				if(org!=null && !org.contains("乐视网信息技术（北京）股份有限公司") && !org.contains("乐视控股（北京）有限公司\\乐视控股（北京）有限公司\\战略管理部")){
//					continue;
//				}
//				if(org!=null && !org.contains("乐视控股（北京）有限公司\\乐视控股（北京）有限公司\\战略管理部")){
//					continue;
//				}
				String emailObj = personObject.getString("username");
				if(StringUtils.isEmpty(emailObj)){
					continue;
				}
				if(emailObj.contains("shiruipeng")){
					System.out.println(emailObj);
				}
		/*		if(emailObj.contains("wangdan16")){
					System.out.println(emailObj);
				}
				if(emailObj.contains("@")){
					continue;
				}*/
			//	String[] es = emailObj.split("@");
			//	String email = es[0];
				//第一次同步，不同步禁用人员 同步日期为：2013年视为第一次同步
	/*			if(personObject.getString("status")!=null && personObject.getString("status").equals("T")){
					String[] times =lastTime.split("-");
					if(times[0].equals("2013")){
						continue;
					}
				}*/
				number++;
				//更新人员
				String[] times =lastTime.split("-");
//				if(!email.equals("guanhui")){
//					continue;
//				}
//				if("guanhui".equals(email)){
//					if(allPerson.containsKey(email)){
//						System.out.println("包含guanhui");
//					}else{
//						System.out.println("不包含guanhui");
//					}
//					
//				}
				String isrepair=ConfigUtil.getValue("isrepair");
				logger.info("isrepair:::::::::::::::::::::::::::::::::::::::"+isrepair);
				boolean mark=false;
				String noEmail=null;
				if(StringUtils.equals("1", isrepair)&&emailObj.indexOf("@")>-1){
					mark=true;
					noEmail=emailObj.substring(0, emailObj.lastIndexOf("@"));
				}
				else{
					System.out.println(emailObj);
				}
				if(allPerson!=null &&( allPerson.containsKey(emailObj)||(mark&&allPerson.containsKey(noEmail)))){//取到时间戳数据如果云之家有的话那就说明有更新
					Person	oldinfo = allPerson.get(emailObj);
					if(oldinfo==null&&emailObj.indexOf("@")>-1){
					oldinfo = allPerson.get(emailObj.substring(0, emailObj.lastIndexOf("@")));
					}
					info.setOpenId(oldinfo.getOpenId());
					//更新状态
					if(true){
						System.out.println("更新人员状态");
	
						uppersonStatusDto.setEid(LetvUtil.KEY);

						if(updatePersonStatus!=null && updatePersonStatus.size()>=10){//10条更新一次
							uppersonStatusDto.setPersons(updatePersonStatus);
							String jsonPer = JSON.toJSONString(uppersonStatusDto);
							updatePersonStatus(jsonPer);
							updatePersonStatus.clear();
							uppersonStatusDto = new PersonData2DTO();
							uppersonStatusDto.setEid(LetvUtil.KEY);
						}
						if("1".equals(oldinfo.getStatus())&&personObject.getString("status")!=null && personObject.getString("status").equals("T")){//离职
							info.setType("3");//人员正常到禁用
							updateStatus++;
							updatePersonStatus.add(info);//更新状态数据集合
							continue;
						}else if(("2".equals(oldinfo.getStatus())||"0".equals(oldinfo.getStatus()))&&personObject.getString("status")!=null && personObject.getString("status").equals("N")){
							info.setType("4");//禁用到正常
							updateStatus++;
							updatePersonStatus.add(info);//更新状态数据集合
							continue;
						}else if(("2".equals(oldinfo.getStatus())||"0".equals(oldinfo.getStatus()))&&personObject.getString("status")!=null && personObject.getString("status").equals("T")){
							System.out.println("两边数据一致不处理...");
							continue;
						}

					}
					//更新组织
					if(true){
						uppersonOrgDto.setEid(LetvUtil.KEY);
						/*if(org!=null&&!"".equals(org)){
							//变动
							org = org.replace("\\", "_");
							String[] str = org.split("_");
							if(str!=null && str.length>0){
								if(str.length==1){
									org = str[0];
								}
								else {
								for(int z=0;z<str.length;z++){
										org = org+"\\"+str[z];
										}
								}
								if(str.length>=4){
									org = str[0]+"\\"+str[1]+"\\"+str[2]+"\\"+str[3];
								}else if(str.length>=3){
									org = str[0]+"\\"+str[1]+"\\"+str[2];
								}else if(str.length>=2){
									org = str[0]+"\\"+str[1];
								}else{
									org = str[0];
								}
							}
						}*/
						logger.info("设置部门"+org);
						System.out.println("设置部门"+org);
						if(updatePersonOrg!=null && updatePersonOrg.size()>=10){
							uppersonOrgDto.setPersons(updatePersonOrg);
							String jo = JSON.toJSONString(uppersonOrgDto);
							updatePersonOrg(jo);
							updatePersonOrg.clear();
							uppersonOrgDto = new PersonData2DTO();
							uppersonOrgDto.setEid(LetvUtil.KEY);
						}
						if(!StringUtils.equals(org,oldinfo.getDepartment())){//部门变动在更新
							info.setDepartment(org);
							updateDept++;
							updatePersonOrg.add(info);
						}


					}
					//更新基本信息
					if(true){
						System.out.println("更新基本信息1");
						if(noSyncPersonPhone!=null && !noSyncPersonPhone.containsKey(emailObj)){
//							info.setOfficePhone1(personObject.getString("phone"));
						}
						uppersonDto.setEid(LetvUtil.KEY);
						//暂不更新人员姓名
//						info.setName(personObject.getString("nickname"));
//						info.setLongName(personObject.getString("nickname"));
						if(updatePerson!=null && updatePerson.size()>=5){
							uppersonDto.setPersons(updatePerson);
							String jsonPer = JSON.toJSONString(uppersonDto);
							updatePersonDatas(jsonPer);
							updatePerson.clear();
							uppersonDto = new PersonData2DTO();
							uppersonDto.setEid(LetvUtil.KEY);
						}
						if(!StringUtils.equals(emailObj, oldinfo.getPhone()) ||!StringUtils.equals(personObject.getString("nickname"), oldinfo.getName()) ||!StringUtils.equals(personObject.getString("job_title"), oldinfo.getJobTitle())){
							if(!StringUtils.equals(emailObj, oldinfo.getPhone())){
								info.setPhone(emailObj);
								}
							info.setEmail(emailObj);
							info.setName(personObject.getString("nickname"));
							info.setJobTitle(personObject.getString("job_title"));
							updateNum++;
							updatePerson.add(info);
						}

					}
					continue;
				}
				if(noSyncPersonPhone!=null && !noSyncPersonPhone.containsKey(emailObj)){
					//info.setOfficePhone1(personObject.getString("phone"));
				}
				//如果不是更新人员信息，则进行再一次判断是否已经再系统中存在，如果存在，则不同步
				if(allPerson.containsKey(emailObj)){
					System.out.println("已经存在相关用户"+emailObj);
					continue;
				}
				info.setPhone(emailObj);
				info.setEmail(emailObj);
				info.setJobTitle(personObject.getString("job_title"));
				if(personObject.getString("sex")!=null && personObject.getString("sex").equals("M")){
					info.setGender("1");
				}else if(personObject.getString("sex")!=null && personObject.getString("sex").equals("F")){
					info.setGender("2");
				}else{
					info.setGender("0");
				}
				info.setName(personObject.getString("nickname"));
			//	info.setLongName(personObject.getString("english_name"));
				if(personObject.getString("status")!=null && personObject.getString("status").equals("T")){
					info.setStatus("2");
				}else{
					info.setStatus("1");
				}

				if(org!=null&&org!=""){
					//变动
				/*	org = org.replace("\\", "_");
					String[] str = org.split("_");
					if(str!=null && str.length>0){
						if(str.length>=4){
							org = str[0]+"\\"+str[1]+"\\"+str[2]+"\\"+str[3];
						}else if(str.length>=3){
							org = str[0]+"\\"+str[1]+"\\"+str[2];
						}else if(str.length>=2){
							org = str[0]+"\\"+str[1];
						}else{
							org = str[0];
						}

						System.out.println(org+"：组织下增加人员："+personObject.getString("nickname"));
					}*/
					info.setDepartment(org);
				}
				if(!"2".equals(info.getStatus())){
					addNum++;
					listPerson.add(info);
				}
			}
		} 
		reader.close(); 
		connectionInvoke.disconnect(); 
		if(json==null||json.size()<100){
			break;
		}
		page++;
		continue;
		}
		if(updatePersonStatus!=null && updatePersonStatus.size()>0){
			uppersonStatusDto.setPersons(updatePersonStatus);
			String jsonUpStatus = JSON.toJSONString(uppersonStatusDto);
			logger.info("更新人员状态信息："+jsonUpStatus);
			System.out.println("更新人员状态信息："+jsonUpStatus);
			updatePersonStatus(jsonUpStatus);
		}

		if(updatePersonOrg!=null && updatePersonOrg.size()>0){
			uppersonOrgDto.setPersons(updatePersonOrg);
			String jsonUpOrg = JSON.toJSONString(uppersonOrgDto);
			logger.info("更新组织信息："+jsonUpOrg);
			System.out.println("更新组织信息："+jsonUpOrg);
			updatePersonOrg(jsonUpOrg);
		}

		if(updatePerson!=null && updatePerson.size()>0){
			uppersonDto.setPersons(updatePerson);
			String jsonPer = JSON.toJSONString(uppersonDto);
			logger.info("更新人员基本信息："+jsonPer);
			System.out.println("更新人员基本信息："+jsonPer);
			updatePersonDatas(jsonPer);
		}
		personDto.setPersons(listPerson);
		String jo = JSON.toJSONString(personDto);
		return jo ;
	}
	private static Map<String,String> getAllOrg(){
		Map<String,String> orgs = new HashMap<String,String>();
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
					orgs.put(person.get("name").toString(), person.get("department").toString());
				}
			}
		}
		return orgs;
	}
	private static String getOrgDep(String orgNumber) throws Exception{
		String orgName = "";
		if(syncAdminMap==null || !syncAdminMap.containsKey(orgNumber)){
			logger.warn("组织编码："+orgNumber+" 不存在");
			System.out.println("组织编码："+orgNumber+" 不存在");
			return "";
		}
		//		String department = "";
		if(syncAdminMap.get(orgNumber)!=null){
			orgName = syncAdminMap.get(orgNumber).getOrg_name();
		}
		//			if(allOrgs!=null && allOrgs.containsValue(orgName)){
		//				department = orgName;
		//			}
		return orgName;
	}
	private static String showResponseResult(HttpResponse response,String orgName)
	{
		if (null == response)
		{
			return "";
		}
		String department = "";
		HttpEntity httpEntity = response.getEntity();
		try
		{
			InputStream inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine()))
			{
				result += line;

			}
			if(result!=null){
				com.alibaba.fastjson.JSONArray array = JSON.parseArray(result);
				for (int i = 0; i < array.size(); i++) {
					Object obj = array.get(i);
					JSONObject json =JSON.parseObject(obj.toString());
					if(json.containsKey("depertment") && json.get("name")!=null){
						if(json.get("name").equals(orgName)){
							department = json.get("depertment").toString();
							break;
						}
					}
				}
			}
			System.out.println(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return department;
	}
}

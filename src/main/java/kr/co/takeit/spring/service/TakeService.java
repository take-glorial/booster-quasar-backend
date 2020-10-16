package kr.co.takeit.spring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.takeit.spring.dao.TakeDAO;
import kr.co.takeit.spring.service.TakeService;
import kr.co.takeit.util.TakeCodeUtil;
import kr.co.takeit.web.ServiceInfo;

@Service
public class TakeService {

	private static final Logger logger = LoggerFactory.getLogger(TakeService.class);

	@Autowired
	private TakeDAO dao;

	public List<LinkedHashMap<String, String>> selectList(String namespace, String paramString){
		return dao.select(namespace, paramString);
	}

	public List<LinkedHashMap<String, String>> selectList(String namespace, Map param){
		return dao.select(namespace, param);
	}

	public Object selectObj(String namespace, String paramString){
		return dao.selectObj(namespace, paramString);
	}

	public Object selectObj(String namespace, Map param){
		return dao.selectObj(namespace, param);
	}

	public ServiceInfo select(ServiceInfo info) {
		return select(info, false, null, null);
	}

	public ServiceInfo select(ServiceInfo info, boolean bolLog, Map userInfo, Map paramMap) {
		ArrayList<ServiceInfo> list = new ArrayList<ServiceInfo>();
		list.add(info);

		ArrayList<ServiceInfo> result = multiSelect(list, bolLog, userInfo, paramMap);
		return result.get(0);
	}

	public ArrayList<ServiceInfo> multiSelect(ArrayList<ServiceInfo> list) {
		return multiSelect(list, false, null, null);
	}

	public ArrayList<ServiceInfo> multiSelect(ArrayList<ServiceInfo> list, boolean bolLog, Map userInfo, Map paramMap) {
		ArrayList<ServiceInfo> result = null;

		StringBuffer sbLog = new StringBuffer();
		if( list != null && list.size() > 0 ){

			result = new ArrayList<ServiceInfo>();

			for(int i=0; i<list.size(); i++ ){
				ServiceInfo info = list.get(i);

				List data = this.selectList(info.getServiceNamespace(), info.getParam());
				info.setData( data );

				sbLog.append("Query ID: "+ info.getServiceNamespace()+", Parameter: " + info.getParam() +", Result: " + (data != null?data.size():"0") +"\r\n");

				result.add(info);
			}
		}

		// Log Insert
		if( bolLog ) {
			try {
				this.insertLog(TakeCodeUtil.LOGTYPE_SELECT, sbLog.toString(), userInfo, paramMap);
			}catch(Exception ex) {}
		}

		return result;
	}

	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public ArrayList<Map> save(ServiceInfo info) {
		return this.save(info, false, null, null);
	}


	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public ArrayList<Map> save(ServiceInfo info, boolean bolLog, Map userInfo, Map paramMap) {
		ArrayList<ServiceInfo> list = new ArrayList<ServiceInfo>();
		list.add(info);

		return multiSave(list, bolLog, userInfo, paramMap);
	}

	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public ArrayList<Map> multiSave(ArrayList<ServiceInfo> list) {
		return this.multiSave(list, false, null, null);
	}

	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public ArrayList<Map> multiSave(ArrayList<ServiceInfo> list, boolean bolLog, Map userInfo, Map paramMap) {
		int nResult = 0;
		ArrayList<Map> rtnList = new ArrayList<Map>();
		StringBuffer sbLog = new StringBuffer();
		if( list != null && list.size() > 0 ){
			for(int cnt = 0; cnt < list.size(); cnt++ ){
				ServiceInfo info = list.get(cnt);

				String namespace 				= info.getServiceNamespace(); // Insert / Update / Delete
				String ins_namespace			= namespace + "Insert";
				String upd_namespace			= namespace + "Update";
				String del_namespace			= namespace + "Delete";

				int nInsert = 0;
				int nUpdate = 0;
				int nDelete = 0;

				Map<String, String> addKeyVal 			= info.getAddKeyVal();
				ArrayList<Map<String, Object>> dataset 	= info.getDataset();

				if( dataset != null ){
					for( int i=0; i<dataset.size(); i++ ){

						Map data = dataset.get(i);

						if( addKeyVal != null ){
							data.putAll(addKeyVal);
						}

						String saveType = (String)data.get("SAVETYPE");
						if("INS".equals(saveType)){
							if( ins_namespace != null && ins_namespace.trim().length() > 0 ) {
								nResult += dao.insert(ins_namespace, data);

								rtnList.add(data);
								nInsert = nInsert + 1;
							}
						}else if("UPD".equals(saveType)){
							if( upd_namespace != null && upd_namespace.trim().length() > 0 ) {
								nResult += dao.update(upd_namespace, data);

								rtnList.add(data);
								nUpdate = nUpdate + 1;
							}
						}else if("DEL".equals(saveType)){
							if( del_namespace != null && del_namespace.trim().length() > 0 ) {
								nResult += dao.delete(del_namespace, data);

								nDelete = nDelete + 1;
							}
						}
					}
				}

				sbLog.append("Insert Query ID: "+ ins_namespace+", Rows: " + nInsert +"\r\n");
				sbLog.append("Update Query ID: "+ upd_namespace+", Rows: " + nUpdate +"\r\n");
				sbLog.append("Delete Query ID: "+ del_namespace+", Rows: " + nDelete +"\r\n");
			}
		}

		// Log Insert
		if( bolLog ) {
			try {
				this.insertLog(TakeCodeUtil.LOGTYPE_SAVE, sbLog.toString(), userInfo, paramMap);
			}catch(Exception ex) {}
		}

		return rtnList;
	}

	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public int insert(String namespace, Map data){
		return dao.insert(namespace, data);
	}

	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public int update(String namespace, Map data){
		return dao.update(namespace, data);
	}

	@Transactional (value="TxManager", rollbackFor = { Exception.class })
	public int delete(String namespace, Map data) {
		return dao.delete(namespace, data);
	}

	public int insertLog(String historyType, String historyDesc, Map userInfo, Map paramMap){
		/*
		 * 001: 로그인
		 * 002: 로그아웃
		 * 003: Select
		 * 004: Save
		 * 005: 기타
		 */
		String loginDevice 	= null;
		String loginBrowser	= null;
		String projectCd	= null;
		String menuCd		= null;
		String formId		= null;
		String userCd		= null;
		String loginDt		= null;
		String loginIp		= null;

		if( userInfo != null && userInfo.get("user_cd") != null ) {
			loginDevice 	= (String)userInfo.get("login_device");
			loginBrowser	= (String)userInfo.get("login_browser");
			userCd			= (String)userInfo.get("user_cd");
			loginDt			= (String)userInfo.get("login_dt");
			loginIp			= (String)userInfo.get("login_ip");
		}else {
			loginDevice 	= (String)paramMap.get("login_device");
			loginBrowser	= (String)paramMap.get("login_browser");
			userCd			= "-no session-";
			loginIp			= (String)paramMap.get("login_ip");
		}

		projectCd 	= (String)paramMap.get("project_cd");
		menuCd 		= (String)paramMap.get("menu_cd");
		formId 		= (String)paramMap.get("form_id");

		Map logMap = new HashMap();
		logMap.put("login_device"	, loginDevice);		// 접속기기
		logMap.put("login_browser"	, loginBrowser);	// 접속환경
		logMap.put("project_cd"		, projectCd);		// 프로젝트코드
		logMap.put("menu_cd"		, menuCd);			// 메뉴코드
		logMap.put("form_id"		, formId);			// 화면ID
		logMap.put("history_type"	, historyType);		// 이력유형
		logMap.put("user_cd"		, userCd);			// 사용자코드
		logMap.put("login_dt"		, loginDt);			// 로그인일시
		logMap.put("history_desc"	, historyDesc);		// 이력내용
		logMap.put("login_ip"		, loginIp);		// 로그인IP

		return dao.insert("Statistics.historyLogInsert", logMap);
	}
}

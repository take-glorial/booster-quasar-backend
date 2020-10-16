/**
 * Nexacro Request/Response 정보를 가지고 있는 객체
 * @filename kr.co.takeit.nexacro.NexacroServiceInfo.java
 * @author Take
 * @since 2019.01.01
 * @version 1.0
 * @see
 * 
 * << 변경 이력(Modification Information) >>
 * 
 * 변경번호 : #1
 * 변경일자 : 2019.01.01
 * 변경사람 : Take
 * 변경내용 : 신규 생성
 * 
 */
package kr.co.takeit.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceInfo {
	/**
	 * DataSet 명칭
	 */
	private String datasetName;
	/**
	 * Query Service ID
	 */
	private String serviceNamespace;
	/**
	 * Parameter
	 */
	private Map param;
	/**
	 * Query결과 데이터
	 */
	private List data;
	/**
	 * 컬럼 목록
	 */
	private List colList;
	/**
	 * 에러코드
	 */
	private int errorCode;
	/**
	 * 에러메세지
	 */
	private String errorMsg;
	/**
	 * 추가해야 할 값
	 */
	private Map<String, String> addKeyVal;
	/**
	 * DataSet 객체
	 */
	private ArrayList<Map<String, Object>> dataset;
	/**
	 * 변환 클래스
	 */
	private Class voClass;
	/**
	 * 페이지 총 카운트
	 */
	private String totalCnt;
	
	/**
	 * 입력 Count
	 */
	private int inputCnt;
	
	/**
	 * 수정 Count
	 */
	private int updateCnt;
	
	/**
	 * 삭제 Count
	 */
	private int deleteCnt;
	
	/**
	 * 생성자 함수
	 * 
	 */
	public ServiceInfo(){
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ########## 공통 Select Setting START ########## 
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 생성자 함수 - 공통 Select Setting
	 *  
	 * @param datasetName	DataSet 명
	 * @param serviceNamespace	Query Namespace
	 */
	public ServiceInfo(String datasetName, String serviceNamespace){
		this.datasetName 		= datasetName;
		this.serviceNamespace	= serviceNamespace;
		this.param				= null;
		this.voClass			= null;
	}
	
	/**
	 * 생성자 함수 - 공통 Select Setting
	 *  
	 * @param datasetName	DataSet 명
	 * @param serviceNamespace	Query Namespace
	 * @param param	Parameter Map 정보
	 */
	public ServiceInfo(String datasetName, String serviceNamespace, Map param){
		this.datasetName 		= datasetName;
		this.serviceNamespace	= serviceNamespace;
		this.param				= param;
		this.voClass			= null;
	}
	
	/**
	 * 생성자 함수 - 공통 Select Setting
	 *  
	 * @param datasetName	DataSet 명
	 * @param serviceNamespace	Query Namespace
	 * @param param	Parameter Map 정보
	 * @param voClass	변환할 클래스
	 */
	public ServiceInfo(String datasetName, String serviceNamespace, Map param, Class voClass){
		this.datasetName 		= datasetName;
		this.serviceNamespace	= serviceNamespace;
		this.param				= param;
		this.voClass			= voClass;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ########## 공통 Select Setting END ########## 
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ########## 공통 Save Setting START ########## 
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 생성자 함수 - 공통 Save Setting
	 *  
	 * @param datasetName	DataSet명
	 * @param serviceNamespace	Query Namespace
	 * @param dataset	DataSet객체
	 */
	public ServiceInfo(String datasetName, String serviceNamespace, ArrayList<Map<String, Object>> dataset){
		this.datasetName		= datasetName;
		this.serviceNamespace	= serviceNamespace;
		this.dataset			= dataset;
		this.addKeyVal			= null;
		this.voClass			= null;
	}
	
	/**
	 * 생성자 함수 - 공통 Save Setting
	 * 
	 * @param datasetName	DataSet명
	 * @param serviceNamespace	Query Namespace
	 * @param dataset	DataSet객체
	 * @param addKeyVal	추가할 값(Map)
	 */
	public ServiceInfo(String datasetName, String serviceNamespace, ArrayList<Map<String, Object>> dataset, Map<String, String> addKeyVal){
		this.datasetName		= datasetName;
		this.serviceNamespace	= serviceNamespace;
		this.dataset			= dataset;
		this.addKeyVal			= addKeyVal;
		this.voClass			= null;
	}
	
	/**
	 * 생성자 함수 - 공통 Save Setting
	 * 
	 * @param datasetName	DataSet명
	 * @param serviceNamespace	Query Namespace
	 * @param dataset	DataSet객체
	 * @param addKeyVal	추가할 값(Map)
	 * @param voClass	변환할 클래스
	 */
	public ServiceInfo(String datasetName, String serviceNamespace, ArrayList<Map<String, Object>> dataset, Map<String, String> addKeyVal, Class voClass){
		this.datasetName		= datasetName;
		this.serviceNamespace	= serviceNamespace;
		this.dataset			= dataset;
		this.addKeyVal			= addKeyVal;
		this.voClass			= voClass;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ########## 공통 Save Setting END ########## 
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 생성자 함수 - 기타일 경우
	 *  
	 * @param datasetName	DataSet명
	 * @param data	처리데이터
	 */
	public ServiceInfo(String datasetName, List data){
		this.datasetName 		= datasetName;
		this.data				= data;
	}

	/**
	 * 생성자 함수 - 메세지 전송용
	 * 
	 * @param errorCode	에러코드
	 * @param errorMsg	에러메세지
	 */
	public ServiceInfo(int errorCode, String errorMsg){
		this.errorCode 	= errorCode;
		this.errorMsg	= errorMsg;
	}
	
	/**
	 * 생성자 함수 - 저장성공 후
	 * 
	 * @param inputCnt	입력건수
	 * @param updateCnt	수정건수
	 * @param deleteCnt	삭제건수
	 */
	public ServiceInfo(int inputCnt, int updateCnt, int deleteCnt){
		this.inputCnt 	= inputCnt;
		this.updateCnt 	= updateCnt;
		this.deleteCnt	= deleteCnt;
	}
	
	/**
	 * Parameter 반환
	 * 
	 * @return	Parameter정보
	 */
	public Map getParam() {
		return param;
	}

	/**
	 * Parameter 세팅
	 * 
	 * @param param	Parameter정보
	 */
	public void setParam(Map param) {
		this.param = param;
	}

	/**
	 * Column List 반환
	 * 
	 * @return	Column List
	 */
	public List getColList() {
		return colList;
	}

	/**
	 * Column List 세팅
	 * 
	 * @param colList	Column List
	 */
	public void setColList(List colList) {
		this.colList = colList;
	}

	/**
	 * DataSet 명 반환
	 * 
	 * @return	DataSet 명
	 */
	public String getDatasetName() {
		return datasetName;
	}

	/**
	 * DataSet 명 세팅
	 * 
	 * @param datasetName	DataSet 명
	 */
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	/**
	 * Query Namespace 반환
	 * 
	 * @return	Query Namespace
	 */
	public String getServiceNamespace() {
		return serviceNamespace;
	}

	/**
	 * Query Namespace 세팅
	 * 
	 * @param serviceNamespace	Query Namespace
	 */
	public void setServiceNamespace(String serviceNamespace) {
		this.serviceNamespace = serviceNamespace;
	}
	
	/**
	 * 추가값 반환
	 * 
	 * @return	추가값
	 */
	public Map<String, String> getAddKeyVal() {
		return addKeyVal;
	}

	/**
	 * 추가값 세팅
	 * 
	 * @param addKeyVal	추가값
	 */
	public void setAddKeyVal(Map<String, String> addKeyVal) {
		this.addKeyVal = addKeyVal;
	}

	/**
	 * DataSet 객체 반환
	 * 
	 * @return	DataSet 객체
	 */
	public ArrayList<Map<String, Object>> getDataset() {
		return dataset;
	}

	/**
	 * DataSet 객체 세팅
	 * 
	 * @param dataset	DataSet 객체
	 */
	public void setDataset(ArrayList<Map<String, Object>> dataset) {
		this.dataset = dataset;
	}

	/**
	 * 변환할 클래스 반환
	 * 
	 * @return	변환할 클래스
	 */
	public Class getVoClass() {
		return voClass;
	}

	/**
	 * 변환할 클래스 세팅
	 * 
	 * @param voClass	변환할 클래스
	 */
	public void setVoClass(Class voClass) {
		this.voClass = voClass;
	}

	/**
	 * Query결과 데이터 반환
	 * 
	 * @return	Query결과 데이터
	 */
	public List getData(){
		return data;
	}
	
	/**
	 * Query결과 데이터 세팅
	 * 
	 * @param data	Query결과 데이터
	 */
	public void setData(List data){
		this.data = data;
	}
	
	/**
	 * 에러코드 반환
	 * 
	 * @return	에러코드
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * 에러코드 세팅
	 * 
	 * @param errorCode	에러코드
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * 에러메세지 반환
	 * 
	 * @return	에러메세지
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * 에러메세지 세팅
	 * 
	 * @param errorMsg	에러메세지
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * 쿼리없이 DataSet 명칭을 넘길 경우
	 * 
	 * @param datasetName	DataSet 명
	 */
	public void setNonQueryInfo(String datasetName){
		this.datasetName 	= datasetName;
	}
	
	/**
	 * 페이지 총 카온트 반환
	 * 
	 * @return	페이지 총 카온트
	 */
	public String getTotalCnt() {
		return totalCnt;
	}

	/**
	 * 페이지 총 카운트 세팅
	 * 
	 * @param totalCnt	페이지 총 카온트
	 */
	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * 입력 Count 반환
	 * 
	 * @return	입력 Count
	 */
	public int getInputCnt() {
		return inputCnt;
	}

	/**
	 * 입력 Count 세팅
	 * 
	 * @param inputCnt	입력 Count
	 */
	public void setInputCnt(int inputCnt) {
		this.inputCnt = inputCnt;
	}

	/**
	 * 수정 Count 반환
	 * 
	 * @return	수정 Count
	 */
	public int getUpdateCnt() {
		return updateCnt;
	}

	/**
	 * 수정 Count 세팅
	 * 
	 * @param updateCnt	수정 Count
	 */
	public void setUpdateCnt(int updateCnt) {
		this.updateCnt = updateCnt;
	}

	/**
	 * 삭제 Count 반환
	 * 
	 * @return	삭제 Count
	 */
	public int getDeleteCnt() {
		return deleteCnt;
	}

	/**
	 * 삭제 Count 세팅
	 * 
	 * @param deleteCnt	삭제 Count
	 */
	public void setDeleteCnt(int deleteCnt) {
		this.deleteCnt = deleteCnt;
	}
}

package kr.co.takeit.spring.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import kr.co.takeit.exception.TakeException;
import kr.co.takeit.util.TakeStringUtil;

@Repository
public class TakeVueDAO {

	private static final Logger logger = LoggerFactory.getLogger(TakeVueDAO.class);

	//@Autowired
	@Resource(name="sqlSession")
	private SqlSession session;

	@Resource
	private SqlSessionFactory factory;

	/**
	 * Parameter문자열 하나로 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param paramString	Parameter문자열
	 * @return	Select결과
	 */
	public List<LinkedHashMap<String, Object>> select(String namespace, String paramString){
		return session.selectList(namespace, paramString);
	}

	/**
	 * Parameter문자열 여러개(Map)로 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param param	Parameter정보(Map)
	 * @return	Select결과
	 */
	public List<LinkedHashMap<String, Object>> select(String namespace, Map param){
		return session.selectList(namespace, param);
	}

	/**
	 * Parameter객체 하나로 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Parameter 객체
	 * @return	Select결과
	 */
	public List<LinkedHashMap<String, Object>> select(String namespace, Object obj){
		return session.selectList(namespace, obj);
	}

	/**
	 * Parameter여러개를 특정 VO Class로 변형 후 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param param	Parameter정보(Map)
	 * @param voClass	변환 클래스
	 * @return	Select결과
	 * @throws TakeException
	 */
	public List<LinkedHashMap<String, Object>> select(String namespace, Map param, Class voClass) throws Exception{
		Object beanObj = voClass.newInstance();
		BeanUtils.populate(beanObj, param);
		return session.selectList(namespace, beanObj);
	}

	/**
	 * Parameter문자열 하나로 Select 수행 (결과필드가 하나일 경우)
	 *
	 * @param namespace	Query Namespace
	 * @param paramString	Parameter문자열
	 * @return	Select결과
	 */
	public Object selectObj(String namespace, String paramString){
		return session.selectOne(namespace, paramString);
	}

	/**
	 * Parameter문자열 여러개(Map)로 Select 수행 (결과필드가 하나일 경우)
	 *
	 * @param namespace	Query Namespace
	 * @param param	Parameter정보(Map)
	 * @return	Select결과
	 */
	public Object selectObj(String namespace, Map param){
		return session.selectOne(namespace, param);
	}

	/**
	 * Parameter객체 하나로 Select 수행 (결과필드가 하나일 경우)
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Parameter 객체
	 * @return	Select결과
	 */
	public Object selectObj(String namespace, Object obj){
		return session.selectOne(namespace, obj);
	}

	/**
	 * Parameter여러개를 특정 VO Class로 변형 후 Select 수행 (결과필드가 하나일 경우)
	 *
	 * @param namespace	Query Namespace
	 * @param param	Parameter정보(Map)
	 * @param voClass	변환 클래스
	 * @return	Select결과
	 * @throws TakeException
	 */
	public Object selectObj(String namespace, Map param, Class voClass) throws Exception{
		Object beanObj = voClass.newInstance();
		BeanUtils.populate(beanObj, param);
		return session.selectOne(namespace, beanObj);
	}

	/**
	 * Object를 통하여 Insert 처리
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int insert(String namespace, Object obj){
		return session.insert(namespace, obj);
	}

	/**
	 * Map정보(data)를 특정 VO Class로 변환후 Insert 처리
	 *
	 * @param namespace	Query Namespace
	 * @param data	Insert정보(Map)
	 * @param voClass	변환할 클래스
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int insert(String namespace, Map data, Class voClass) throws Exception{
		Object beanObj = voClass.newInstance();
		BeanUtils.populate(beanObj, data);
		return session.insert(namespace, beanObj);
	}

	/**
	 * Object를 통하여 Update 처리
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int update(String namespace, Object obj){
		return session.update(namespace, obj);
	}

	/**
	 * Map정보(data)를 특정 VO Class로 변환후 Update 처리
	 *
	 * @param namespace	Query Namespace
	 * @param data	Update정보(Map)
	 * @param voClass	변환할 클래스
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int update(String namespace, Map data, Class voClass) throws Exception{
		Object beanObj = voClass.newInstance();
		BeanUtils.populate(beanObj, data);
		return session.update(namespace, beanObj);
	}

	/**
	 * Object를 통하여 Delete 처리
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int delete(String namespace, Object obj){
		return session.delete(namespace, obj);
	}

	/**
	 * Map정보(data)를 특정 VO Class로 변환후 Delete 처리
	 *
	 * @param namespace	Query Namespace
	 * @param data	Delete정보(Map)
	 * @param voClass	변환할 클래스
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int delete(String namespace, Map data, Class voClass) throws Exception{
		Object beanObj = voClass.newInstance();
		BeanUtils.populate(beanObj, data);
		return session.delete(namespace, beanObj);
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 입력/수정/삭제 처리
	 *
	 * @param namespaceMap	Insert/Update/Delete Query Namespace
	 * @param dataset	입력/수정/삭제 정보
	 * @param paramMap	추가해야할 값
	 * @return	처리결과
	 */
	public int save(Map<String, String> namespaceMap, ArrayList<Map<String, Object>> dataset, Map paramMap){
		int nResult = 0;

		String ins_namespace = TakeStringUtil.nvl(namespaceMap.get("INS"));
		String upd_namespace = TakeStringUtil.nvl(namespaceMap.get("UPD"));
		String del_namespace = TakeStringUtil.nvl(namespaceMap.get("DEL"));

		if( dataset != null ){
			for( int i=0; i<dataset.size(); i++ ){
				Map data = dataset.get(i);	/* mybatis에 넘기기 위해 Map으로 변환 */

				if( paramMap != null ){
					data.putAll(paramMap);
				}

				String rowType = (String)data.get("_RowType_");

				if( "I".equals(rowType) ) {
					if( ins_namespace != null && ins_namespace.trim().length() > 0 )
						nResult += session.insert(ins_namespace, data);
				}else if ( "U".equals(rowType) ) {
					if( upd_namespace != null && upd_namespace.trim().length() > 0 )
						nResult += session.update(upd_namespace, data);
				}else if ( "D".equals(rowType) ) {
					if( del_namespace != null && del_namespace.trim().length() > 0 )
						nResult += session.delete(del_namespace, data);
				}
			}
		}

		return nResult;
	}
}
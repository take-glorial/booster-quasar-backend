package kr.co.takeit.spring.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.takeit.exception.TakeException;
import kr.co.takeit.util.TakeStringUtil;

@Repository
public class TakeDAO {

	private static final Logger logger = LoggerFactory.getLogger(TakeDAO.class);

	//@Autowired
	@Resource(name="sqlSession")
	private SqlSession session;

	@Resource(name="batchSession")
	private SqlSession batchSession;

	@Resource
	private SqlSessionFactory factory;

	/**
	 * Parameter문자열 하나로 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param paramString	Parameter문자열
	 * @return	Select결과
	 */
	public List<LinkedHashMap<String, String>> select(String namespace, String paramString){
		return session.selectList(namespace, paramString);
	}

	/**
	 * Parameter문자열 여러개(Map)로 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param param	Parameter정보(Map)
	 * @return	Select결과
	 */
	public List<LinkedHashMap<String, String>> select(String namespace, Map param){
		return session.selectList(namespace, param);
	}

	/**
	 * Parameter객체 하나로 Select 수행
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Parameter 객체
	 * @return	Select결과
	 */
	public List<LinkedHashMap<String, String>> select(String namespace, Object obj){
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
	public List<LinkedHashMap<String, String>> select(String namespace, Map param, Class voClass) throws TakeException{
		Object beanObj = null;

		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, param);
		}catch(IllegalAccessException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InstantiationException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InvocationTargetException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}

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
	public Object selectObj(String namespace, Map param, Class voClass) throws TakeException{
		Object beanObj = null;

		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, param);
		}catch(IllegalAccessException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InstantiationException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InvocationTargetException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}

		return session.selectOne(namespace, beanObj);
	}

	/**
	 * Map정보(data)를 통하여 Insert 처리
	 *
	 * @param namespace	Query Namespace
	 * @param data	Insert정보(Map)
	 * @return	처리결과
	 */
	public int insert(String namespace, Map data){
		return session.insert(namespace, data);
	}

	/**
	 * Object를 통하여 Insert 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int commitInsert(String namespace, Map data){

		int nResult = 0;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			nResult = commitSession.insert(namespace, data);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitInsert() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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
	 * Object를 통하여 Insert 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int commitInsert(String namespace, Object obj){
		int nResult = 0;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			nResult = commitSession.insert(namespace, obj);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitInsert() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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
	public int insert(String namespace, Map data, Class voClass) throws TakeException{
		Object beanObj = null;

		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, data);
		}catch(IllegalAccessException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InstantiationException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InvocationTargetException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}

		return session.insert(namespace, beanObj);
	}

	/**
	 * Map정보(data)를 특정 VO Class로 변환후 Insert 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param data	Insert정보(Map)
	 * @param voClass	변환할 클래스
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int commitInsert(String namespace, Map data, Class voClass) throws TakeException{
		int nResult = 0;
		Object beanObj = null;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, data);

			nResult = commitSession.insert(namespace, beanObj);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitInsert() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
	}

	/**
	 * Map정보(data)를 통하여 Update 처리
	 *
	 * @param namespace	Query Namespace
	 * @param data	Update정보(Map)
	 * @return	처리결과
	 */
	public int update(String namespace, Map data){
		return session.update(namespace, data);
	}

	/**
	 * Map정보(data)를 통하여 Update 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param data	Update정보(Map)
	 * @return	처리결과
	 */
	public int commitUpdate(String namespace, Map data){
		int nResult = 0;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			nResult = commitSession.update(namespace, data);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitUpdate() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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
	 * Object를 통하여 Update 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int commitUpdate(String namespace, Object obj){
		int nResult = 0;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			nResult = commitSession.update(namespace, obj);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitUpdate() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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
	public int update(String namespace, Map data, Class voClass) throws TakeException{
		Object beanObj = null;

		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, data);
		}catch(IllegalAccessException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InstantiationException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InvocationTargetException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}

		return session.update(namespace, beanObj);
	}

	/**
	 * Map정보(data)를 특정 VO Class로 변환후 Update 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param data	Update정보(Map)
	 * @param voClass	변환할 클래스
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int commitUpdate(String namespace, Map data, Class voClass) throws TakeException{
		int nResult = 0;
		Object beanObj = null;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, data);

			nResult = commitSession.update(namespace, beanObj);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitUpdate() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
	}

	/**
	 * Map정보(data)를 통하여 Delete 처리
	 *
	 * @param namespace	Query Namespace
	 * @param data	Delete정보(Map)
	 * @return	처리결과
	 */
	public int delete(String namespace, Map data){
		return session.delete(namespace, data);
	}

	/**
	 * Map정보(data)를 통하여 Delete 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param data	Delete정보(Map)
	 * @return	처리결과
	 */
	public int commitDelete(String namespace, Map data){
		int nResult = 0;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			nResult = commitSession.delete(namespace, data);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitDelete() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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
	 * Object를 통하여 Delete 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param obj	Object정보
	 * @return	처리결과
	 */
	public int commitDelete(String namespace, Object obj){
		int nResult = 0;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			nResult = commitSession.delete(namespace, obj);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitDelete() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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
	public int delete(String namespace, Map data, Class voClass) throws TakeException{
		Object beanObj = null;

		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, data);
		}catch(IllegalAccessException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InstantiationException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}catch(InvocationTargetException e){
			logger.error("TakeDAOImple.select() ERROR: " + e.getMessage());
			throw new TakeException("TakeDAOImple.select() ERROR: " + e.getMessage());
		}

		return session.delete(namespace, beanObj);
	}

	/**
	 * Map정보(data)를 특정 VO Class로 변환후 Delete 처리 (단독으로 커밋 처리)
	 *
	 * @param namespace	Query Namespace
	 * @param data	Delete정보(Map)
	 * @param voClass	변환할 클래스
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int commitDelete(String namespace, Map data, Class voClass) throws TakeException{
		int nResult = 0;
		Object beanObj = null;

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			beanObj = voClass.newInstance();
			BeanUtils.populate(beanObj, data);

			nResult = commitSession.delete(namespace, beanObj);
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitDelete() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
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

	/**
	 * DataSet을 가지고 Row의 타입에 따라 입력/수정/삭제 처리 (단독으로 커밋 처리)
	 *
	 * @param namespaceMap	Insert/Update/Delete Query Namespace
	 * @param dataset	입력/수정/삭제 정보
	 * @param paramMap	추가해야할 값
	 * @return	처리결과
	 */
	public int commitSave(Map<String, String> namespaceMap, ArrayList<Map<String, Object>> dataset, Map paramMap){

		int nResult = 0;
		String ins_namespace = TakeStringUtil.nvl(namespaceMap.get("INS"));
		String upd_namespace = TakeStringUtil.nvl(namespaceMap.get("UPD"));
		String del_namespace = TakeStringUtil.nvl(namespaceMap.get("DEL"));

		SqlSession commitSession = factory.openSession(ExecutorType.SIMPLE);
		try{
			if( dataset != null ){
				for( int i=0; i<dataset.size(); i++ ){
					Map data = dataset.get(i);	/* mybatis에 넘기기 위해 Map으로 변환 */

					if( paramMap != null ){
						data.putAll(paramMap);
					}

					String rowType = (String)data.get("_RowType_");

					if( "I".equals(rowType) ) {
						if( ins_namespace != null && ins_namespace.trim().length() > 0 )
							nResult += commitSession.insert(ins_namespace, data);
					}else if ( "U".equals(rowType) ) {
						if( upd_namespace != null && upd_namespace.trim().length() > 0 )
							nResult += commitSession.update(upd_namespace, data);
					}else if ( "D".equals(rowType) ) {
						if( del_namespace != null && del_namespace.trim().length() > 0 )
							nResult += commitSession.delete(del_namespace, data);
					}
				}
			}
		}catch(Exception ex){
			commitSession.rollback();
			logger.error("TakeDAOImple.commitSave() ERROR: " + ex.toString());
		}finally{
			commitSession.flushStatements();
			commitSession.close();
		}

		return nResult;
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 입력/수정/삭제 Batch 처리
	 *
	 * @param namespaceMap	Insert/Update/Delete Query Namespace
	 * @param dataset	입력/수정/삭제 정보
	 * @param paramMap	추가해야할 값
	 * @return	처리결과
	 */
	@Transactional
	public int batchDataset(Map<String, String> namespaceMap, ArrayList<Map<String, Object>> dataset, Map paramMap) {
		int nResult = 0;

		String ins_namespace = TakeStringUtil.nvl(namespaceMap.get("INS"));
		String upd_namespace = TakeStringUtil.nvl(namespaceMap.get("UPD"));
		String del_namespace = TakeStringUtil.nvl(namespaceMap.get("DEL"));

		long startTime = System.currentTimeMillis();

		if( dataset != null ){
			for( int i=0; i<dataset.size(); i++ ){
				Map data = dataset.get(i);	/* mybatis에 넘기기 위해 Map으로 변환 */

				if( paramMap != null ){
					data.putAll(paramMap);
				}

				String rowType = (String)data.get("_RowType_");

				if( "I".equals(rowType) ) {
					if( ins_namespace != null && ins_namespace.trim().length() > 0 )
						nResult += batchSession.insert(ins_namespace, data);
				}else if ( "U".equals(rowType) ) {
					if( upd_namespace != null && upd_namespace.trim().length() > 0 )
						nResult += batchSession.update(upd_namespace, data);
				}else if ( "D".equals(rowType) ) {
					if( del_namespace != null && del_namespace.trim().length() > 0 )
						nResult += batchSession.delete(del_namespace, data);
				}
			}
		}
//			batchSession.flushStatements();

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;
        logger.debug("####################### TakeDAOImpl(batchDataset) ##########################");
        logger.debug("# startTime =" + startTime);
        logger.debug("# endTime =" + endTime);
        logger.debug("# 소요시간  : " + resutTime/1000 + "(ms)");
        logger.debug("#################################################################");

		return nResult;
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 입력 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param dataset	입력정보
	 * @param paramMap	추가해야할 값
	 * @return	처리결과
	 */
	@Transactional
	public int batchInsert(String namespace, ArrayList<Map<String, Object>> dataset, Map paramMap) {
		int nResult = 0;

		long startTime = System.currentTimeMillis();

		if( dataset != null ){
			for( int i=0; i<dataset.size(); i++ ){
				Map data = dataset.get(i);	/* mybatis에 넘기기 위해 Map으로 변환 */

				if( paramMap != null ){
					data.putAll(paramMap);
				}

				nResult += batchSession.insert(namespace, data);
			}
		}
//			batchSession.flushStatements();

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;

        logger.info("트랜젝션 배치" + " 소요시간  : " + resutTime/1000 + "(ms)");

		return nResult;
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 수정 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param dataset	수정정보
	 * @param paramMap	추가해야할 값
	 * @return	처리결과
	 */
	@Transactional
	public int batchUpdate(String namespace, ArrayList<Map<String, Object>> dataset, Map paramMap) {
		int nResult = 0;

		long startTime = System.currentTimeMillis();

		if( dataset != null ){
			for( int i=0; i<dataset.size(); i++ ){
				Map data = dataset.get(i);	/* mybatis에 넘기기 위해 Map으로 변환 */

				if( paramMap != null ){
					data.putAll(paramMap);
				}

				nResult += batchSession.update(namespace, data);
			}
		}
//			batchSession.flushStatements();

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;

        logger.info("트랜젝션 배치" + " 소요시간  : " + resutTime/1000 + "(ms)");

		return nResult;
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 삭제 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param dataset	삭제정보
	 * @param paramMap	추가해야할 값
	 * @return	처리결과
	 */
	@Transactional
	public int batchDelete(String namespace, ArrayList<Map<String, Object>> dataset, Map paramMap) {
		int nResult = 0;

		long startTime = System.currentTimeMillis();

		if( dataset != null ){
			for( int i=0; i<dataset.size(); i++ ){
				Map data = dataset.get(i);	/* mybatis에 넘기기 위해 Map으로 변환 */

				if( paramMap != null ){
					data.putAll(paramMap);
				}

				nResult += batchSession.delete(namespace, data);
			}
		}
//		batchSession.flushStatements();

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;

        logger.info("트랜젝션 배치" + " 소요시간  : " + resutTime/1000 + "(ms)");

		return nResult;
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 입력/수정/삭제 Batch 처리
	 *
	 * @param namespaceMap	Insert/Update/Delete Query Namespace
	 * @param dataset	입력/수정/삭제 정보
	 * @return	처리결과
	 */
	public int batchDataset(Map<String, String> namespaceMap, ArrayList<Map<String, Object>> dataset) {
		return batchDataset(namespaceMap, dataset, null);
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 입력 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param dataset	입력정보
	 * @return	처리결과
	 */
	public int batchInsert(String namespace, ArrayList<Map<String, Object>> dataset) {
		return batchInsert(namespace, dataset, null);
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 수정 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param dataset	수정정보
	 * @return	처리결과
	 */
	public int batchUpdate(String namespace, ArrayList<Map<String, Object>> dataset) {
		return batchUpdate(namespace, dataset, null);
	}

	/**
	 * DataSet을 가지고 Row의 타입에 따라 삭제 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param dataset	삭제정보
	 * @return	처리결과
	 */
	public int batchDelete(String namespace, ArrayList<Map<String, Object>> dataset) {
		return batchDelete(namespace, dataset, null);
	}

	/**
	 * List에 있는 정보르 가지고 각 행에 있는 SAVETYPE에 따라 입력/수정/삭제 Batch 처리
	 *
	 * @param namespaceMap 입력/수정/삭제 Query Namespace
	 * @param list	입력/수정/삭제 정보
	 * @param paramMap	추가 정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchList(Map<String, String> namespaceMap, List list, Map paramMap) throws TakeException{
		return batchList(namespaceMap, list, paramMap, 0);
	}

	/**
	 * List에 있는 정보르 가지고 입력 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력정보
	 * @param paramMap	추가정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	private int batch(String type, String namespace, List list, Map paramMap) throws TakeException{
		return batch(type, namespace, list, paramMap, 0);
	}

	/**
	 * List에 있는 정보르 가지고 입력 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력정보
	 * @param paramMap	추가정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchInsert(String namespace, List list, Map paramMap) throws TakeException{
		return batch("INS", namespace, list, paramMap, 0);
	}

	/**
	 * List에 있는 정보르 가지고 수정 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	수정정보
	 * @param paramMap	추가정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchUpdate(String namespace, List list, Map paramMap) throws TakeException{
		return batch("UPD", namespace, list, paramMap, 0);
	}

	/**
	 * List에 있는 정보르 가지고 삭제 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	수정정보
	 * @param paramMap	추가정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchDelete(String namespace, List list, Map paramMap) throws TakeException{
		return batch("DEL", namespace, list, paramMap, 0);
	}

	/**
	 * List에 있는 정보르 가지고 각 행에 있는 SAVETYPE에 따라 입력/수정/삭제 Batch 처리
	 *
	 * @param namespaceMap 입력/수정/삭제 Query Namespace
	 * @param list	입력/수정/삭제 정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchList(Map<String, String> namespaceMap, List list) throws TakeException{
		return batchList(namespaceMap, list, null, 0);
	}

	/**
	 * List에 있는 정보르 가지고 입력 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchInsert(String namespace, List list) throws TakeException{
		return batchInsert(namespace, list, null, 0);
	}

	/**
	 * List에 있는 정보르 가지고 수정 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchUpdate(String namespace, List list) throws TakeException{
		return batchUpdate(namespace, list, null, 0);
	}

	/**
	 * List에 있는 정보르 가지고 삭제 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력정보
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchDelete(String namespace, List list) throws TakeException{
		return batchDelete(namespace, list, null, 0);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력/수정/삭제 Batch 처리
	 *
	 * @param namespaceMap	입력/수정/삭제 Query Namespace
	 * @param list	입력/수정/삭제 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchList(Map<String, String> namespaceMap, List list, Map paramMap, int cnt) throws TakeException{
		int nResult = 0;

		String ins_namespace = TakeStringUtil.nvl(namespaceMap.get("INS"));
		String upd_namespace = TakeStringUtil.nvl(namespaceMap.get("UPD"));
		String del_namespace = TakeStringUtil.nvl(namespaceMap.get("DEL"));

		long startTime = System.currentTimeMillis();

		SqlSession cntSession = factory.openSession(ExecutorType.BATCH);

		int cntStart	= 0;
		int cntEnd		= 0;
		int cntScope	= 0;

		try{
			if( cnt == 0 ){
				cnt = list.size();
			}

			if( list != null ){

				cntEnd		= cnt;
				cntScope	= (list.size() / cnt);

				logger.debug("# cnt = " + cnt);
				logger.debug("# list.size() = " + list.size());
				logger.debug("# cntStart = " + cntStart);
				logger.debug("# cntEnd = " + cntEnd);
				logger.debug("# cntScope = " + cntScope);

				for(int xx=0; xx < cntScope+1; xx++) {

					if( xx == cntScope)	cntEnd = list.size();
					else				cntEnd = cnt * (xx + 1);

					for( ; cntStart< cntEnd; cntStart++){
						Map data = (Map)list.get(cntStart);

						if( paramMap != null ){
							data.putAll(paramMap);
						}

						String saveType = (String)data.get("SAVETYPE");
						if("INS".equals(saveType)){
							if( ins_namespace != null && ins_namespace.trim().length() > 0 )
								nResult += cntSession.insert(ins_namespace, data);
						}else if("UPD".equals(saveType)){
							if( upd_namespace != null && upd_namespace.trim().length() > 0 )
								nResult += cntSession.update(upd_namespace, data);
						}else if("DEL".equals(saveType)){
							if( del_namespace != null && del_namespace.trim().length() > 0 )
								nResult += cntSession.delete(del_namespace, data);
						}
					}

					// Commit
					cntSession.commit();
				}
			}
		}catch(Exception ex){
			// Rollback
			cntSession.rollback();
			logger.error("TakeDAOImple.batchList() ERROR: " + ex.toString());
			throw new TakeException("TakeDAOImple.batchList() ERROR: " + ex.getMessage());
		}finally{
			cntSession.flushStatements();
			cntSession.close();
		}

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;
        logger.debug("####################### TakeDAOImpl(batchList) ##########################");
        logger.debug("# startTime =" + startTime);
        logger.debug("# endTime =" + endTime);
        logger.debug("# 소요시간  : " + resutTime/1000 + "(ms)");
        logger.debug("#################################################################");


		return nResult;
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력/수정/사게 Batch 처리
	 *
	 * @param type	입력(INS)/수정(UPD)/삭제(DEL)
	 * @param namespace Query Namespace
	 * @param list	입력 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	private int batch(String type, String namespace, List list, Map paramMap, int cnt) throws TakeException{
		int nResult = 0;

		long startTime = System.currentTimeMillis();

		SqlSession cntSession = factory.openSession(ExecutorType.BATCH);

		int cntStart	= 0;
		int cntEnd		= 0;
		int cntScope	= 0;

		try{

			if( cnt == 0 ){
				cnt = list.size();
			}

			if( list != null ){
				cntEnd		= cnt;
				cntScope	= (list.size() / cnt);

				logger.debug("# cnt = " + cnt);
				logger.debug("# list.size() = " + list.size());
				logger.debug("# cntStart = " + cntStart);
				logger.debug("# cntEnd = " + cntEnd);
				logger.debug("# cntScope = " + cntScope);

				for(int xx=0; xx < cntScope+1; xx++) {

					if( xx == cntScope)		cntEnd = list.size();
					else					cntEnd = cnt * (xx + 1);

					for( ; cntStart< cntEnd; cntStart++){

						Map data = (Map)list.get(cntStart);

						if("INS".equals(type)){
							nResult += cntSession.insert(namespace, data);
						}else if("UPD".equals(type)){
							nResult += cntSession.update(namespace, data);
						}else if("DEL".equals(type)){
							nResult += cntSession.delete(namespace, data);
						}
					}

					// Commit
					cntSession.commit();
				}
			}
		}catch(Exception ex){
			// Rollback
			cntSession.rollback();
			logger.error("TakeDAOImple.batchList() ERROR: " + ex.toString());
			throw new TakeException("TakeDAOImple.batch() ERROR: " + ex.getMessage());
		}finally{
			cntSession.flushStatements();
			cntSession.close();
		}

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;
        logger.debug("####################### TakeDAOImpl(batch) ##########################");
        logger.debug("# startTime =" + startTime);
        logger.debug("# endTime =" + endTime);
        logger.debug("# 소요시간  : " + resutTime/1000 + "(ms)");
        logger.debug("#################################################################");

		return nResult;
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param list	입력 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchInsert(String namespace, List list, Map paramMap, int cnt) throws TakeException{
		return batch("INS", namespace, list, paramMap, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 수정 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param list	수정 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws Exception
	 */
	public int batchUpdate(String namespace, List list, Map paramMap, int cnt) throws TakeException{
		return batch("UPD", namespace, list, paramMap, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 삭제 Batch 처리
	 *
	 * @param namespaceMap	Query Namespace
	 * @param list	삭제 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws Exception
	 */
	public int batchDelete(String namespace, List list, Map paramMap, int cnt) throws TakeException{
		return batch("DEL", namespace, list, paramMap, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력/수정/삭제 Batch 처리
	 *
	 * @param namespaceMap	입력/수정/삭제 Query Namespace
	 * @param list	입력/수정/삭제 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchList(Map<String, String> namespaceMap, List list, int cnt) throws TakeException{
		return batchList(namespaceMap, list, null, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchInsert(String namespace, List list, int cnt) throws TakeException{
		return batchInsert(namespace, list, null, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 수정 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	수정 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchUpdate(String namespace, List list, int cnt) throws TakeException{
		return batchUpdate(namespace, list, null, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 삭제 Batch 처리
	 *
	 * @param namespace	Query Namespace
	 * @param list	삭제 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchDelete(String namespace, List list, int cnt) throws TakeException{
		return batchDelete(namespace, list, null, cnt);
	}

	/////////////////////////////////////////////////////////////////////////////////
	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력/수정/사게 Batch 처리 (사용하지 않음)
	 *
	 * @param namespaceMap Query Namespace
	 * @param list	입력 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return
	 * @throws TakeException
	 */
	public int batchListTran(Map<String, String> namespaceMap, List list, Map paramMap, int cnt) throws TakeException{
		int nResult = 0;

		String ins_namespace = TakeStringUtil.nvl(namespaceMap.get("INS"));
		String upd_namespace = TakeStringUtil.nvl(namespaceMap.get("UPD"));
		String del_namespace = TakeStringUtil.nvl(namespaceMap.get("DEL"));

		long startTime = System.currentTimeMillis();

		SqlSession cntSession = factory.openSession(ExecutorType.BATCH);

		int cntStart	= 0;
		int cntEnd		= 0;
		int cntScope	= 0;

		try{

			if( cnt == 0 ){
				cnt = list.size();
			}

			if( list != null ){

				cntEnd		= cnt;
				cntScope	= (list.size() / cnt);

				logger.debug("# cnt = " + cnt);
				logger.debug("# list.size() = " + list.size());
				logger.debug("# cntStart = " + cntStart);
				logger.debug("# cntEnd = " + cntEnd);
				logger.debug("# cntScope = " + cntScope);

				for(int xx=0; xx < cntScope+1; xx++) {

					if( xx == cntScope)	cntEnd = list.size();
					else				cntEnd = cnt * (xx + 1);

					for( ; cntStart< cntEnd; cntStart++){
						Map data = (Map)list.get(cntStart);

						if( paramMap != null ){
							data.putAll(paramMap);
						}

						String saveType = (String)data.get("SAVETYPE");
						if("INS".equals(saveType)){
							if( ins_namespace != null && ins_namespace.trim().length() > 0 )
								nResult += cntSession.insert(ins_namespace, data);
							break;
						}else if("UPD".equals(saveType)){
							if( upd_namespace != null && upd_namespace.trim().length() > 0 )
								nResult += cntSession.update(upd_namespace, data);
							break;
						}else if("DEL".equals(saveType)){
							if( del_namespace != null && del_namespace.trim().length() > 0 )
								nResult += cntSession.delete(del_namespace, data);
							break;
						}
					}

					// Commit
					cntSession.commit();
				}
			}
		}catch(Exception ex){
			// Rollback
			cntSession.rollback();
			logger.error("TakeDAOImple.batchList() ERROR: " + ex.toString());
			throw new TakeException("TakeDAOImple.batchList() ERROR: " + ex.getMessage());
		}finally{
			cntSession.flushStatements();
			cntSession.close();
		}

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;
        logger.debug("####################### TakeDAOImpl(batchList) ##########################");
        logger.debug("# startTime =" + startTime);
        logger.debug("# endTime =" + endTime);
        logger.debug("# 소요시간  : " + resutTime/1000 + "(ms)");
        logger.debug("#################################################################");


		return nResult;
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력/수정/사게 Batch 처리 (사용하지 않음)
	 *
	 * @param type	입력(INS)/수정(UPD)/삭제(DEL)
	 * @param namespace Query Namespace
	 * @param list	입력 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	private int batchTran(String type, String namespace, List list, Map paramMap, int cnt) throws TakeException{
		int nResult = 0;

		long startTime = System.currentTimeMillis();

		SqlSession cntSession = factory.openSession(ExecutorType.BATCH);

		int cntStart	= 0;
		int cntEnd		= 0;
		int cntScope	= 0;

		try{

			if( list != null ){
				cntEnd		= cnt;
				cntScope	= (list.size() / cnt);

				logger.debug("# cnt = " + cnt);
				logger.debug("# list.size() = " + list.size());
				logger.debug("# cntStart = " + cntStart);
				logger.debug("# cntEnd = " + cntEnd);
				logger.debug("# cntScope = " + cntScope);

				for(int xx=0; xx < cntScope+1; xx++) {

					if( xx == cntScope)		cntEnd = list.size();
					else					cntEnd = cnt * (xx + 1);

					for( ; cntStart< cntEnd; cntStart++){

						Map data = (Map)list.get(cntStart);

						if("INS".equals(type)){
							nResult += cntSession.insert(namespace, data);
						}else if("UPD".equals(type)){
							nResult += cntSession.update(namespace, data);
						}else if("DEL".equals(type)){
							nResult += cntSession.delete(namespace, data);
						}
					}

					// Commit
					cntSession.commit();
				}
			}
		}catch(Exception ex){
			// Rollback
			cntSession.rollback();
			logger.error("TakeDAOImple.batchList() ERROR: " + ex.toString());
			throw new TakeException("TakeDAOImple.batchList() ERROR: " + ex.getMessage());
		}finally{
			cntSession.flushStatements();
			cntSession.close();
		}

		long endTime = System.currentTimeMillis();
        long resutTime = endTime - startTime;
        logger.debug("####################### TakeDAOImpl(batch) ##########################");
        logger.debug("# startTime =" + startTime);
        logger.debug("# endTime =" + endTime);
        logger.debug("# 소요시간  : " + resutTime/1000 + "(ms)");
        logger.debug("#################################################################");

		return nResult;
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력 Batch 처리 (사용하지 않음)
	 *
	 * @param namespace	Query Namespace
	 * @param list	입력 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchInsertTran(String namespace, List list, Map paramMap, int cnt) throws TakeException{
		return batchTran("INS", namespace, list, paramMap, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 수정 Batch 처리 (사용하지 않음)
	 *
	 * @param namespace	Query Namespace
	 * @param list	수정 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchUpdateTran(String namespace, List list, Map paramMap, int cnt) throws TakeException{
		return batchTran("UPD", namespace, list, paramMap, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 삭제 Batch 처리 (사용하지 않음)
	 *
	 * @param namespace	Query Namespace
	 * @param list	삭제 정보
	 * @param paramMap	추가정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchDeleteTran(String namespace, List list, Map paramMap, int cnt) throws TakeException{
		return batchTran("DEL", namespace, list, paramMap, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력/수정/삭제 Batch 처리 (사용하지 않음)
	 *
	 * @param namespaceMap	입력/수정/삭제 Query Namespace
	 * @param list	입력/수정/삭제 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchListTran(Map<String, String> namespaceMap, List list, int cnt) throws TakeException{
		return batchListTran(namespaceMap, list, null, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 입력 Batch 처리 (사용하지 않음)
	 *
	 * @param namespaceMap	Query Namespace
	 * @param list	입력 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchInsertTran(String namespace, List list, int cnt) throws TakeException{
		return batchInsertTran(namespace, list, null, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 수정 Batch 처리 (사용하지 않음)
	 *
	 * @param namespaceMap	Query Namespace
	 * @param list	수정 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchUpdateTran(String namespace, List list, int cnt) throws TakeException{
		return batchUpdateTran(namespace, list, null, cnt);
	}

	/**
	 * 일정 개수(cnt)에 따라 트랜잭션 처리하며 삭제 Batch 처리 (사용하지 않음)
	 *
	 * @param namespaceMap	Query Namespace
	 * @param list	삭제 정보
	 * @param cnt	트랜잭션 처리 수
	 * @return	처리결과
	 * @throws TakeException
	 */
	public int batchDeleteTran(String namespace, List list, int cnt) throws TakeException{
		return batchDeleteTran(namespace, list, null, cnt);
	}

	/**
	 * Namespace에 정의된 Query 문자열 출력
	 *
	 * @param namespace	Query Namespace
	 * @param paramMap	Parameter문자열
	 * @return	Query 문자열
	 */
	public String getSql(String namespace, Map paramMap){
		BoundSql boundSql = session.getConfiguration().getMappedStatement(namespace).getSqlSource().getBoundSql(paramMap);

		String query = boundSql.getSql();
		Object param = boundSql.getParameterObject();

		if( param != null ){
			if( param instanceof Integer || param instanceof Long || param instanceof Float || param instanceof Double ){
				query = query.replaceFirst("\\?", param.toString());
			}else if( param instanceof String ){
				query = query.replaceFirst("\\?", "'"+param.toString()+"'");
			}else if( param instanceof Map ){
				List<ParameterMapping> paramMapping = boundSql.getParameterMappings();

				for(ParameterMapping mapping : paramMapping){
					String propKey = mapping.getProperty(); // <foreach>인 경우 propKey가 "__frch_%아이템명%_반복횟수"
					Object propVal = paramMap.get(propKey);

					if( propVal == null ){
						// __frch_ PREFIX에 대한 처리
						if(boundSql.hasAdditionalParameter(propKey)) {  // myBatis가 추가 동적인수를 생성했는지 체크하고
							propVal = boundSql.getAdditionalParameter(propKey);  // 해당 추가 동적인수의 Value를 가져옴
						}

						if(propVal == null) continue;
					}

					query = query.replaceFirst("\\?", "'" + (String)propVal + "'");
				}
			}

		}else{
			query = query.replaceFirst("\\?", "''");
		}

		return query;
	}
}
package kr.co.takeit.spring.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.takeit.spring.dao.TakeVueDAO;

@Service
public class TakeVueService {

	private static final Logger logger = LoggerFactory.getLogger(TakeVueService.class);

	@Autowired
	private TakeVueDAO dao;

	public List<LinkedHashMap<String, Object>> selectList(String namespace, Map param){
		return dao.select(namespace, param);
	}

	public void saveList(Map<String, Object> paramMap, List<Map<String, Object>> paramList){
		for(Map<String, Object> params : paramList) {
			String nameSpace = (String)params.get("aNameSpace");
			Map<String, Object> data = (Map<String, Object>)params.get("oOutDs");
			//Map<String, Object> param = (Map<String, Object>)params.get("sParam");
			data.putAll(paramMap);
			dao.update(nameSpace, data);
		}
	}
}

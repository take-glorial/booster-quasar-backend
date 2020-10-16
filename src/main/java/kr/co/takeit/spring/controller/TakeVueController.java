/**
 * EasyBAM 공통 컨트롤러
 * @filename kr.co.takeit.spring.controller.TakeVueController.java
 * @author Take
 * @since 2020.08.10
 * @version 1.0
 * @see
 *
 * << 변경 이력(Modification Information) >>
 *
 * 변경번호 : #1
 * 변경일자 : 2020.08.10
 * 변경사람 : Take
 * 변경내용 : 신규 생성
 *
 */
package kr.co.takeit.spring.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.takeit.spring.service.TakeVueService;
import kr.co.takeit.vue.VueServiceManager;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class TakeVueController {

	/**
	 * Log 객체
	 */
	private static final Logger logger = LoggerFactory.getLogger(TakeVueController.class);

	/**
	 * 공통 서비스 인터페이스
	 */
	@Autowired
	TakeVueService service;

	/**
	 * take transelect
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("select")
	public Map<String, Object> select(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("select");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> paramMap = manager.getParamMap(request);

		List<Map<String, Object>> paramList = (List<Map<String, Object>>)paramMap.get("sParams");
		paramMap.remove("sParams");

		Map<String, Object> resultInfo = new HashMap<>();
		for(Map<String, Object> params : paramList) {

			String nameSpace = (String)params.get("aNameSpace");
			String inDs = (String)params.get("sInDs");

			Map<String, Object> param = (Map<String, Object>)params.get("sParam");
			param.putAll(paramMap);

			List list = service.selectList(nameSpace, param);
			resultInfo.put(inDs, list);
		}

		return manager.success(resultInfo);
	}

	/**
	 * take transave
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("save")
	public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("save");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List<Map<String, Object>> paramList = (List<Map<String, Object>>)param.get("sParams");
		param.remove("sParams");

		service.saveList(param, paramList);

		return manager.success();
	}
}

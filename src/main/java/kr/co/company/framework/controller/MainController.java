package kr.co.company.framework.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.takeit.spring.service.TakeService;
import kr.co.takeit.vue.VueServiceManager;

@RestController
@RequestMapping("main")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	private TakeService service;

	/**
	 * User getBoardList
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping("getBoardList")
	public Map<String, Object> getBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("getBoardList");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List noticeList = service.selectList("BoardManagement.boardInfoList", param);
		List boardList = service.selectList("BoardManagement.boardInfoList", param);

		Map<String, Object> resultInfo = new HashMap<>();
		resultInfo.put("noticeList", noticeList);
		resultInfo.put("boardList", boardList);

		return manager.success(resultInfo);
	}

	/**
	 * 마이메뉴 입력
	 *
	 * @param request	HttpServletRequest 객체
	 * @param response	HttpServletResponse 객체
	 * @throws ParseException
	 * @throws IOException
	 */
	@PostMapping("myMenuInsert")
	public Map<String, Object> myMenuInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("myMenuInsert");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List<LinkedHashMap<String, String>> list = service.selectList("Main.userMymenuGroupList", param);
		if( list == null || list.size() == 0 ) {
			service.insert("Main.userMymenuGroupInsert", param);
		}
		service.insert("Main.userMymenuInsert", param);

		return manager.success();
	}

	/**
	 * 마이메뉴 입력
	 *
	 * @param request	HttpServletRequest 객체
	 * @param response	HttpServletResponse 객체
	 */
	@PostMapping("myMenuDelete")
	public Map<String, Object> myMenuDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("myMenuDelete");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		service.delete("Main.userMymenuDelete", param);

		return manager.success();
	}
}

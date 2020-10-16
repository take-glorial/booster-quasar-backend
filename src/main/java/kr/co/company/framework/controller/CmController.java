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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.util.ImmutableMapParameter;

import kr.co.company.framework.service.CmService;
import kr.co.takeit.common.service.FileService;
import kr.co.takeit.spring.service.TakeVueService;
import kr.co.takeit.vue.VueServiceManager;

@RestController
@RequestMapping("cm")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CmController {

	private static final Logger logger = LoggerFactory.getLogger(CmController.class);

	@Autowired
	TakeVueService takeVueService;

	@Autowired
	FileService fileService;

	@Autowired
	CmService service;

	/**
	 * 게시판 생성
	 *
	 * @param request	HttpServletRequest 객체
	 * @param response	HttpServletResponse 객체
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("boardInsert")
	public void boardInsert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Cm boardInsert");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		Map<String, Object> noticeInfo = (Map<String, Object>)param.get("noticeInfo");
		List<Map<String, Object>> targetInfo = (List<Map<String, Object>>)param.get("targetInfo");

		service.boardInfoInsert(noticeInfo, targetInfo);

		manager.success();
	}

	/**
	 * 게시판 상세
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 *
	 */
	@RequestMapping("boardSelect")
	@ResponseBody
	public Map<String, Object> boardSelect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List<LinkedHashMap<String, Object>> boardList = takeVueService.selectList("BoardManagement.boardInfoDetail", param);
		List<LinkedHashMap<String, Object>> replyList = takeVueService.selectList("BoardManagement.boardReplyList", param);
		List<LinkedHashMap<String, Object>> targetList = takeVueService.selectList("BoardManagement.boardTargetList", param);

		//게시판 조회 수 증가
		service.readUser(param);

		//파일 조회
		LinkedHashMap<String, Object> boardInfo = boardList.get(0);
		List<LinkedHashMap<String, Object>> fileList = fileService
				.getAtchFileList(ImmutableMapParameter.of("attach_cd", (String) boardInfo.get("board_attach")));

		Map<String, Object> resultInfo = new HashMap<>();
		resultInfo.put("boardInfo", boardInfo);
		resultInfo.put("replyList", replyList);
		resultInfo.put("fileList", fileList);
		resultInfo.put("targetList", targetList);

		return manager.success(resultInfo);
	}

	/**
	 * 게시판 저장
	 *
	 * @param request	HttpServletRequest 객체
	 * @param response	HttpServletResponse 객체
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("boardUpdate")
	public void boardUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Cm boardUpdate");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		Map<String, Object> noticeInfo = (Map<String, Object>)param.get("noticeInfo");
		List<Map<String, Object>> targetInfo = (List<Map<String, Object>>)param.get("targetInfo");

		service.boardInfoUpdate(noticeInfo, targetInfo);

		manager.success();
	}

	/**
	 * 게시판 삭제
	 *
	 * @param request	HttpServletRequest 객체
	 * @param response	HttpServletResponse 객체
	 */
	@RequestMapping("boardDelete")
	public void boardDelete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Cm boardDelete");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		service.boardInfoDelete(param);

		manager.success();
	}
}

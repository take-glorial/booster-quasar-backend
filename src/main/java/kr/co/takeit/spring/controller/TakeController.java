/**
 * EasyBAM 공통 컨트롤러
 * @filename kr.co.takeit.spring.controller.TakeController.java
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
package kr.co.takeit.spring.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.takeit.spring.service.TakeService;
import kr.co.takeit.util.ProcessApplication;
import kr.co.takeit.util.ResRepHandler;
import kr.co.takeit.util.ResponseCode;
import kr.co.takeit.util.SessionManager;
import kr.co.takeit.util.TakeJsonUtil;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
public class TakeController {

	/**
	 * Log 객체
	 */
	private static final Logger logger = LoggerFactory.getLogger(TakeController.class);

	@Autowired
	private SessionManager sessionMgr;

	/**
	 * 공통 서비스 인터페이스
	 */
	@Autowired
	private TakeService service;

	/**
	 * 파일 업로드 경로
	 */
	@Value("${file.upload.path}")
	private String fileUploadPath;

	/**
	 * 임시저장경로
	 */
	@Value("${tmp.upload.path}")
	private String tempUploadPath;

	@PostMapping("/")
	@ResponseBody
	public String select() {
		return "hello";
	}

//	/**
//	 * 공통 Select - dsTranConfig Dataset이 있을 경우에는 해당 Dataset을 Loop 처리하면서 다중의 Select
//	 * 건을 조회한 후 넘긴다. - dsTranConfig Dataset이 없을 경우에는 단일 Select건 조회한다.
//	 *
//	 * @param request  HttpServletRequest 객체
//	 * @param response HttpServletResponse 객체
//	 */
//	@PostMapping(path = "/pm/projectMenuSelect.do", produces = "text/html; charset=utf8")
//	@ResponseBody
//	public String select(HttpServletRequest request, HttpServletResponse response) {
//		logger.info("Common select");
//
////		ArrayList<ServiceInfo> list = new ArrayList<ServiceInfo>();
//
//		Map userInfo = null;
//
//		String sMenuCd = null;
//		String sMenuType = null;
//		String sProjectCd = null;
//		String sDeviceType = null;
//		String sUseYn = null;
//
//		Map<String, String> paramMap = new HashMap<String, String>();
//		List list = null;
//		try {
//			userInfo = sessionMgr.getUserInfo(request);
//
//			sMenuCd = request.getParameter("menu_cd");
//			sMenuType = request.getParameter("menu_type");
//			sProjectCd = request.getParameter("project_cd");
//			sDeviceType = request.getParameter("device_type");
//			sUseYn = request.getParameter("use_yn");
//
//			paramMap.put("menu_cd", sMenuCd);
//			paramMap.put("menu_type", sMenuType);
//			paramMap.put("project_cd", sProjectCd);
//			paramMap.put("device_type", sDeviceType);
//			paramMap.put("use_yn", sUseYn);
//
//			list = service.selectList("ProjectManagement.projectMenuList", paramMap);
//
//		} catch (Exception ex) {
//			logger.error(ex.getMessage());
//		}
//
//		String str = TakeJsonUtil.getJsonStringFromList(list);
//		System.out.println("================================================");
//		System.out.println(str);
//		System.out.println("================================================");
//		return str;
//	}

	/**
	 * User login
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping(path = "/main/login.do", produces = "application/json; charset=utf8")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		logger.info("Login");

		List listResult = ResRepHandler.handleRequest(service, request, "Main.userLogin");

		if (listResult != null && !listResult.isEmpty()) {
			JSONArray userJsArray = TakeJsonUtil.getJsonArrayFromList(listResult);
			Map userInfoMap = TakeJsonUtil.getMapStringFromJsonObject((JSONObject) userJsArray.get(0));
			String token = ProcessApplication.generateToken(userInfoMap);

			return ResRepHandler.handleResponse(listResult, token);
		}
		return null;
	}

	/**
	 * User logout
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping(path = "/main/logout.do", produces = "application/json; charset=utf8")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		logger.info("Login");
	}

	/**
	 * User menu
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping(path = "/main/userMenuSelect.do", consumes = "application/json", produces = "application/json; charset=utf8")
	@ResponseBody
	public List userMenuSelect(HttpServletRequest request, HttpServletResponse response) {
		logger.info("userMenuSelect");
		try {
			return ResRepHandler.responseData(service, request, "Main.userMenuSelect");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}

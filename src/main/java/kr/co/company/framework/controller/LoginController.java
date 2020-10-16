package kr.co.company.framework.controller;

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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.takeit.spring.service.TakeVueService;
import kr.co.takeit.util.ProcessApplication;
import kr.co.takeit.vue.VueServiceManager;

@RestController
@RequestMapping("login")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	TakeVueService service;

	/**
	 * User login
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping("login")
	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Login");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List userList = service.selectList("Login.userLogin", param);

		if (!CollectionUtils.isEmpty(userList)) {
			Map<String, Object> userInfo = (Map<String, Object>) userList.get(0);
			String token = ProcessApplication.generateToken(userInfo);

			return manager.message(false, token, userInfo);
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
	@PostMapping("logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("logout");
	}

	/**
	 * User getUserInfo
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping("getUserInfo")
	public Map<String, Object> getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("getUserInfo");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List userList = service.selectList("Login.userInfo", param);
		List menuList = service.selectList("Login.userMenuSelect", param);

		if (!CollectionUtils.isEmpty(userList)) {
			Map<String, Object> loginInfo = new HashMap<>();
			loginInfo.put("userInfo", userList.get(0));
			loginInfo.put("menuInfo", menuList);

			return manager.success(loginInfo);
		}
		return null;
	}

	/**
	 * User getMenuInfo
	 *
	 * @param request  HttpServletRequest 객체
	 * @param response HttpServletResponse 객체
	 * @throws IOException
	 * @throws ParseException
	 */
	@PostMapping("getMenuInfo")
	public Map<String, Object> getMenuInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("getMenuInfo");

		VueServiceManager manager = new VueServiceManager();
		Map<String, Object> param = manager.getParamMap(request);

		List menuList = service.selectList("Login.projectMenuSelect", param);

		if (!CollectionUtils.isEmpty(menuList)) {
			Map<String, Object> menuInfo = new HashMap<>();
			menuInfo.put("menuInfo", menuList);

			return manager.success(menuInfo);
		}
		return null;
	}
}

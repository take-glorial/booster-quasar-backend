package kr.co.takeit.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import kr.co.takeit.util.ProcessApplication;
import kr.co.takeit.util.SessionManager;
import kr.co.takeit.util.TakeStringUtil;

public class SessionInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);
	
	@Autowired
	private SessionManager sessionMgr;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		
		System.out.println("###########################################");
		System.out.println("### TEST ####");
		System.out.println("###########################################");
		
		String url = request.getRequestURL().toString();
		if(request.getMethod().equals("OPTIONS"))
			return true;
		if(url.contains("login.do"))
			return true;
		
//		return true;
//
//		String authTokenHeader = request.getHeader("Authorization");
//		if(authTokenHeader!=null) {
//			DecodedJWT jwt = ProcessApplication.verifyToken(authTokenHeader);
//			Claim d = jwt.getClaim("user_cd");
//			long current_time = new Date().getTime() / 1000;
//			if(jwt == null || jwt.getExpiresAt() == null || (jwt != null && jwt.getExpiresAt() != null && current_time > jwt.getExpiresAt().getTime())) {
//				return false;
//			}
//			else
//				return true;
//		}

		return true;
	}
}

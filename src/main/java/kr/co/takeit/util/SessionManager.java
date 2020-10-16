package kr.co.takeit.util;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.co.takeit.web.listener.SessionCountListener;

@Component
public class SessionManager {
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

	public static String sessionObjectName 	= "__SESSION_USERDATA__";

	/**
	 * 사용자정보를 세션에 저장
	 *
	 * @param request
	 * @param info
	 */
	public void createSession(HttpServletRequest request, Map info){
		HttpSession session = request.getSession(true);
		session.setAttribute(sessionObjectName, info);

		SessionCountListener.setSessionUserList((String)info.get("user_cd"), info);
	}

	/**
	 * 지정한 세션명으로 세션정보 저장
	 *
	 * @param request
	 * @param sessionName
	 * @param sessionObject
	 */
	public void createSession(HttpServletRequest request, String sessionName, Object sessionObject){
		HttpSession session = request.getSession(true);
		session.setAttribute(sessionName, sessionObject);
	}

	/**
	 * 세션있는지 여부 체크
	 *
	 * @param request
	 * @return
	 */
	public boolean existSession(HttpServletRequest request){
		boolean isExist = false;

		HttpSession session = request.getSession(false);

		if( session != null ){
			Map info = (Map)session.getAttribute(sessionObjectName);
			if( info != null && info.get("USER_ID") != null && ((String)info.get("USER_ID")).trim().length() > 0 ){
				isExist = true;
			}
		}

		return isExist;
	}

	/**
	 * 세션에 있는 사용자 정보 반환
	 *
	 * @param request
	 * @return
	 */
	public Map getUserInfo(HttpServletRequest request){
		HttpSession session = request.getSession(false);

		Map info = null;
		if( session != null ){
			info = (Map)session.getAttribute(sessionObjectName);
		}

		return info;
	}

	/**
	 * 세션에 있는 사용자ID 반환
	 *
	 * @param request
	 * @return
	 */
	public String getUserId(HttpServletRequest request){
		String userId = null;
		HttpSession session = request.getSession(false);

		if( session != null ){
			Map info = (Map)session.getAttribute(sessionObjectName);

			if( info != null ){
				userId = (String)info.get("user_id");
			}
		}

		return userId;
	}

	/**
	 * 세션에 있는 Cust ID 반환
	 *
	 * @param request
	 * @return
	 */
	public String getUserCd(HttpServletRequest request){
		String userCd = null;
		HttpSession session = request.getSession(false);

		if( session != null ){
			Map info = (Map)session.getAttribute(sessionObjectName);

			if( info != null ){
				userCd = (String)info.get("user_cd");
			}
		}

		return userCd;
	}

	/**
	 * 세션에 있는 Cust명 반환
	 *
	 * @param request
	 * @return
	 */
	public String getUserNm(HttpServletRequest request){
		String userNm = null;
		HttpSession session = request.getSession(false);

		if( session != null ){
			Map info = (Map)session.getAttribute(sessionObjectName);

			if( info != null ){
				userNm = (String)info.get("user_nm");
			}
		}

		return userNm;
	}

	/**
	 * 지정한 세션명의 값 반환
	 *
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public String getSessionName(HttpServletRequest request, String sessionName){
		String returnName = null;
		HttpSession session = request.getSession(false);

		if( session != null ){
			returnName = (String)session.getAttribute(sessionName);
		}

		return returnName;
	}

	/**
	 * 세션 삭제
	 *
	 * @param request
	 */
	public void deleteSession(HttpServletRequest request){
		HttpSession session = request.getSession(false);

		if( session != null) {
			session.invalidate();
		}
	}
}
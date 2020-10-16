package kr.co.takeit.web.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCountListener implements HttpSessionListener{
	public static int count = 0;
	public static Map<String, Object> sessionUserList = new HashMap<String, Object>();

    public static int getCount() {
        return count;
    }
    
    public static Map<String, Object> getSessionUserList() {
        return sessionUserList;
    }
    
    public static void setSessionUserList( String userId, Map vo) {
    	System.out.println("!@!@>> sessionUserList.size:  " + sessionUserList.size());
    	
    	sessionUserList.put(userId, vo);
    }
    
    public static void delSessionUserList( String userId ) {
    	sessionUserList.remove(userId);
    }

    public void sessionCreated(HttpSessionEvent event) {
        //세션이 만들어질 때 호출
        HttpSession session = event.getSession(); //request에서 얻는 session과 동일한 객체
        //session.setMaxInactiveInterval(180);
        
        try {
            count++;
            
            System.out.println( "###" + session.getId() + " 세션생성 " + ", 접속자수 : " + count + ", time: " + session.getMaxInactiveInterval());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			sessionDestroyed(event);
		}
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        //세션이 소멸될 때 호출
        count--;
        if(count<0)
            count=0;

        HttpSession session = event.getSession();
        
        Map info = (Map)session.getAttribute("__SESSION_USERDATA__");
        
        if( info != null ){
	        sessionUserList.remove((String)info.get("USER_ID"));
	        
	        session.getServletContext().log(session.getId() + " 세션소멸 " + ", 접속자수 : " + count);
	        System.out.println("!@!@>> sessionDestroyed:  " + info.get("USER_ID") + "(" + session.getId() + ")");
        }
    }
}


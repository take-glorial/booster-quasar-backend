package kr.co.takeit.web.listener;

import java.net.InetAddress;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.io.ClassPathResource;

import kr.co.takeit.util.TakeFileUtil;

public class TakeConfigListener implements ServletContextListener {
	
	public TakeConfigListener(){
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		// 1) License Check
		String licenseLocation = context.getInitParameter("licenseLocation");
		context.log("::: License File -> " + licenseLocation);
		context.log("::: context.getContextPath() -> " + context.getRealPath("/"));
		try{
			
			context.setAttribute("__EASYBAM_LICENSE_CHECK__", "true");
			
			String resourcePath = new ClassPathResource("/log/log4jdbc.log4j2.properties").getURI().getPath();
			context.log("::: Resource Path -> " + resourcePath);
			context.log("::: Resource value -> " + TakeFileUtil.readText(resourcePath));
		}catch(Exception ex){}
		
		if( TakeFileUtil.checkEasyBAM("HK") == false ){
			throw new RuntimeException(":::::::::: License Check Error ::::::::::");
		}
		
		if( licenseLocation != null ){
			// 2) Log Setting
			String logConfigLocation = context.getInitParameter("logConfigLocation");
			
			if(logConfigLocation != null){
				// Write log message to server log.
				context.log("::: Set System Property -> log4jdbc.log4j2.properties.file=" + logConfigLocation);
				
				// System Property 설정: log4jdbc-log4j2 Property 경로 변경 설정
				System.setProperty("log4jdbc.log4j2.properties.file", logConfigLocation);
			}else{
				context.log("::: Not Found 'log4jdbcLog4j2ConfigLocation' parameter: log4jdbc.log4j2 properties file will be loaded from default location");
			}
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}
	
}

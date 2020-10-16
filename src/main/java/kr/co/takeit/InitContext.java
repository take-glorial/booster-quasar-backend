package kr.co.takeit;

import org.springframework.stereotype.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class InitContext {
	
	private static final Logger log = LoggerFactory.getLogger(InitContext.class);
	
	public void init() throws Exception {
		
		log.info("##########################################################################");
		log.info("############### EasyBAM InitContext Start ###############");
		log.info("##########################################################################");
		log.info(" ※ 여기에는 시스템에 시작될때 필요한 기능들을 추가합니다.");
		
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


		log.info("##########################################################################");
		log.info("###############  EasyBAM InitContext End  ###############");
		log.info("##########################################################################");
	}
}

package kr.co.takeit.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class TakeEncryptUtil {
	private static final Logger log = LoggerFactory.getLogger( TakeEncryptUtil.class );
	
	/**
	 * 암호화 처리
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText){
		log.info("##########################################");
		log.info("## BCrypt encrypt START" );
		
		String encryptText = null;
		try{
			log.info(" - plainText: " + plainText );
			encryptText = BCrypt.hashpw(plainText, BCrypt.gensalt(10));
			log.info(" - encryptText: " + encryptText );
		}catch(Exception ex){
			log.error(ex.getMessage());
		}
		log.info("## BCrypt encrypt END" );
		log.info("##########################################");
		
		return encryptText;
	}
	
	/**
	 * 비교대상 평문과 암호화 문자열을 비교하여 동일 문자열인지 체크(true/false)
	 * 
	 * @param encHash
	 * @param inputText
	 * @return
	 */
	public static boolean check(String encHash, String inputText){
		log.info("##########################################");
		log.info("## BCrypt check START" );
		log.info(" - encHash: " + encHash );
		log.info(" - inputText: " + inputText );
		
		boolean bolCheck = false;
		try{
			bolCheck = BCrypt.checkpw(inputText, encHash);
			
			log.info(" - bolCheck: " + bolCheck );
		}catch(Exception ex){
			log.error(ex.getMessage());
		}
		log.info("## BCrypt check END" );
		log.info("##########################################");
		
		return bolCheck;
	}
}
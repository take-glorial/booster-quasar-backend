package kr.co.takeit.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class ProcessApplication {

	private static String key = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private static String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
    private static byte[] secretBytes = DatatypeConverter.parseBase64Binary(base64Key);

    public static String generateToken(Map<String, Object> userInfoMap) {
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 2));
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            String token = JWT.create()
                .withIssuer("auth0")
                .withHeader(userInfoMap)
                .withExpiresAt(exp)
                .sign(algorithm);

            return token;

        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }
        return "";
    }

    public static DecodedJWT verifyToken(String token) {
    	try {
    	    Algorithm algorithm = Algorithm.HMAC256(key);
    	    JWTVerifier verifier = JWT.require(algorithm)
    	        .withIssuer("auth0")
    	        .build(); //Reusable verifier instance
    	    DecodedJWT jwt = verifier.verify(token);
    	    return jwt;
    	} catch (JWTVerificationException exception){
    	    //Invalid signature/claims
    		System.out.print(exception.getMessage());
    	}
    	return null;
    }
}

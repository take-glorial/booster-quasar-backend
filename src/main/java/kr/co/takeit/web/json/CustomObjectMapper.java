package kr.co.takeit.web.json;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = -7963919143339158920L;

	public CustomObjectMapper(){
        SimpleModule simpleModule = new SimpleModule();
        registerModule(simpleModule);
    }

}

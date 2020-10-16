package kr.co.takeit.web.json;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import kr.co.takeit.web.JsonLowerCase;

@Configuration
public class CustomConverter extends MappingJackson2HttpMessageConverter {
	@Override
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws HttpMessageNotWritableException, IOException {
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		System.out.println("slkdfjlskdjflksdjfkl");
		objectMapper.setPropertyNamingStrategy(new JsonLowerCase());
	    super.writeInternal(object, type, outputMessage);
	}
}

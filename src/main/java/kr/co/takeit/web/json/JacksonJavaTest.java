package kr.co.takeit.web.json;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class JacksonJavaTest {
	    public static void testSnakeCaseDeserialization() throws IOException {
	        String json = "{\"my_camel_case\":10001}";
	        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
	        TestSnakeCase obj = objectMapper.readValue(json, TestSnakeCase.class);
	    }

	    public static void testSnakeCaseSerialization() throws JsonProcessingException {
	        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
	        TestSnakeCase obj = new TestSnakeCase(10001);
	        System.out.println(objectMapper.writeValueAsString(obj));
	    }

	    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	    static class TestSnakeCase {
	        private Integer myCamelCase;

	        @JsonCreator
	        public TestSnakeCase(Integer myCamelCase) {
	            this.myCamelCase = myCamelCase;
	        }

	        public Integer getMyCamelCase() {
	            return myCamelCase;
	        }

	        public void setMyCamelCase(Integer myCamelCase) {
	            this.myCamelCase = myCamelCase;
	        }
	    }

	    public static void main(String[] args) throws Exception {
	    	JacksonJavaTest.testSnakeCaseSerialization();
		}

}

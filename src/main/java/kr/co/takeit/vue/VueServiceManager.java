package kr.co.takeit.vue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.co.takeit.util.TakeJsonUtil;

public class VueServiceManager {

	public Map<String, Object> getParamMap(HttpServletRequest request) throws IOException, ParseException {
		String body = "";
		if ("POST".equalsIgnoreCase(request.getMethod()))
		{
		   body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		}
		JSONParser parser = new JSONParser();
		if(!body.isEmpty()) {
			JSONObject jsonData = (JSONObject) parser.parse(body);
			Map<String, Object> params = TakeJsonUtil.getMapStringFromJsonObject(jsonData);

			return params;
		}

		return null;
	}

	public Map<String, Object> success() {
		return message(false, "", null);
	}

	public Map<String, Object> success(Map<String, Object> info) {
		return message(false, "", info);
	}

	public Map<String, Object> err(String message) {
		return message(true, message, null);
	}

	public Map<String, Object> message(boolean isError, String message, Map<String, Object> info) {
		Map<String, Object> resultInfo = new HashMap<String, Object>();
		resultInfo.put("error", isError);
		resultInfo.put("message", message);
		resultInfo.put("data", info);
		return resultInfo;
	}
}

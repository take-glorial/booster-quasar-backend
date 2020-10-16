package kr.co.takeit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.co.takeit.spring.service.TakeService;

public class ResRepHandler {

	@SuppressWarnings("unchecked")
	public static String handleResponseError(ResponseCode rc, String message) {
		JSONObject json = new JSONObject();
		json.put("error", true);
		json.put("errorCode", rc.name());
		json.put("message", message);
		return json.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static String handleResponse(JSONArray jsonArray, String message) {
		JSONObject json = new JSONObject();
		json.put("error", false);
		json.put("data", jsonArray);
		json.put("message", message);
		return json.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public static String handleResponse(JSONObject jsonObject, String message) {
		JSONObject json = new JSONObject();
		json.put("error", false);
		json.put("data", jsonObject);
		json.put("message", message);
		return json.toJSONString();
	}

	public static Map<String, Object> handleResponse(Map<String, Object> info) {
		Map<String, Object> resultInfo = new HashMap<String, Object>();
		resultInfo.put("error", false);
		resultInfo.put("data", info);

		return resultInfo;
	}

	public static Map<String, Object> handleResponse(Map<String, Object> info, String token) {
		Map<String, Object> resultInfo = new HashMap<String, Object>();
		resultInfo.put("error", false);
		resultInfo.put("data", info);
		resultInfo.put("message", token);

		return resultInfo;
	}

	public static Map<String, Object> handleResponse(List info, String token) {
		Map<String, Object> resultInfo = new HashMap<String, Object>();
		resultInfo.put("error", false);
		resultInfo.put("data", info);
		resultInfo.put("message", token);

		return resultInfo;
	}

	public static Map<String, Object> getParam(HttpServletRequest request) throws IOException, ParseException {
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

	public static List handleRequest(TakeService service, HttpServletRequest request, String nameSpace) throws IOException, ParseException {
		Map<String, Object> dataMap = getParam(request);
		return service.selectList(nameSpace, dataMap);
	}

	public static List handleRequest(TakeService service, Map<String, Object> dataMap, String nameSpace) throws IOException, ParseException {
		return service.selectList(nameSpace, dataMap);
	}

	public static List responseData(TakeService service, HttpServletRequest request, String nameSpace) throws IOException, ParseException {
		return handleRequest(service, request, nameSpace);
	}

}

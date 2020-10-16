package kr.co.takeit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.common.Collections;

public class TakeJsonUtil {

	private static final Logger logger = LoggerFactory.getLogger( TakeJsonUtil.class );

	/**
     * Map을 json으로 변환한다.
     *
     * @param map Map<String, Object>.
     * @return JSONObject.
     */
    @SuppressWarnings("unchecked")
	public static JSONObject getJsonStringFromMap( Map<String, Object> map )
    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && value.getClass() == String[].class) {
            	JSONArray jsArray = new JSONArray();
            	ArrayList<String> newArray = new ArrayList<String>();
            	  for(String str: (String[])value)
            	  {
            		  newArray.add(str);
            	  }
            	jsArray.addAll(newArray);
            	jsonObject.put(key.toLowerCase(), jsArray);
            }
            else
            	jsonObject.put(key.toLowerCase(), value != null ? value.toString() : value);
        }

        return jsonObject;
    }
    @SuppressWarnings("unchecked")
    public static JSONObject getJsonStringFromMapLowerCase( Map<String, Object> map )
    {
    	JSONObject jsonObject = new JSONObject();
    	for( Map.Entry<String, Object> entry : map.entrySet() ) {
    		String key = entry.getKey();
    		Object value = entry.getValue();
    		if (value != null && value.getClass() == String[].class) {
    			JSONArray jsArray = new JSONArray();
    			ArrayList<String> newArray = new ArrayList<String>();
    			for(String str: (String[])value)
    			{
    				newArray.add(str);
    			}
    			jsArray.addAll(newArray);
    			jsonObject.put(key.toLowerCase(), jsArray);
    		}
    		else
    			jsonObject.put(key.toLowerCase(), value != null ? value.toString() : value);
    	}

    	return jsonObject;
    }

    /**
     * List<Map>을 jsonArray로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return JSONArray.
     */
    public static JSONArray getJsonArrayFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = new JSONArray();
        for( Map<String, Object> map : list ) {
            jsonArray.add( getJsonStringFromMap( map ) );
        }

        return jsonArray;
    }

    /**
     * List<Map>을 jsonString으로 변환한다.
     *
     * @param list List<Map<String, Object>>.
     * @return String.
     */
    public static String getJsonStringFromList( List<Map<String, Object>> list )
    {
        JSONArray jsonArray = getJsonArrayFromList( list );
        return jsonArray.toJSONString();
    }

    /**
     * JsonObject를 Map<String, String>으로 변환한다.
     *
     * @param jsonObj JSONObject.
     * @return Map<String, Object>.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj )
    {
        Map<String, Object> map = null;

        try {

            map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class) ;

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }


    /**
     * JsonObject를 Map<String, String>으로 변환한다.
     *
     * @param jsonObj JSONObject.
     * @return Map<String, Object>.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapStringFromJsonObject( JSONObject jsonObj )
    {
        Map<String, Object> map = null;
        try {

            map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * JsonArray를 List<Map<String, String>>으로 변환한다.
     *
     * @param jsonArray JSONArray.
     * @return List<Map<String, Object>>.
     */
    public static List<Map<String, Object>> getListMapFromJsonArray( JSONArray jsonArray )
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if( jsonArray != null )
        {
            int jsonSize = jsonArray.size();
            for( int i = 0; i < jsonSize; i++ )
            {
                Map<String, Object> map = TakeJsonUtil.getMapFromJsonObject( ( JSONObject ) jsonArray.get(i) );
                list.add( map );
            }
        }

        return list;
    }
}
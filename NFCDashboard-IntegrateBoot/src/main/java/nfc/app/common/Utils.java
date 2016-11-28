package nfc.app.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Utils {
	public static JSONObject convertStringToJsonObject(String jsonStr){
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject() ;
		try {
			json = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			System.out.print("Error " + e.getMessage());
		}
		return json;
	}
	public static Object convertJsonObjectToClass(JSONObject jsonObj, String className, String[] keyRestrict){
		try {
			Class c=Class.forName(className);
			Object instance = c.newInstance();
			Set<String> keys =  jsonObj.keySet();
			
			for(String key:keys)
			{
				if(!Arrays.asList(keyRestrict).contains(key))
				{
					Field field = c.getDeclaredField(key);
		            field.setAccessible(true);
		            field.set(instance,jsonObj.get(key));
				}
			}
			return instance;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

package nfc.serviceImpl.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class Utils {
	public static String appId="e6271952-d4b9-4ed3-b83b-63a56d47a713";
	public static String uploadUrl="/uploads/images/";
	public static String convertObjectToJsonString(Object obj){
		Gson gson = new Gson();
		String jsonInString = gson.toJson(obj);
		return jsonInString;
		//return "";
	}
	public static String ConvertObjectToInsertSQL(Object obj, String table)
	{
		String sql = "";
		Field[] fields = obj.getClass().getDeclaredFields();
		String lstField="";
		String lstValue="";
		for (int i = 0; i < fields.length; i++) {
			try{
				if (!Collection.class.isAssignableFrom(fields[i].getType())) {
					fields[i].setAccessible(true);
					String name = fields[i].getName();
					String value = fields[i].get(obj)!=null?fields[i].get(obj).toString():null;
					if(i!=0){
		        	   lstField=lstField+",";
		        	   lstValue=lstValue+",";
					}
					if(fields[i].get(obj) instanceof String){
		        	   lstValue=lstValue+"'"+value+"'";
					}
					else{
		        	   lstValue=lstValue+value;
	            	}
					lstField=lstField+name;
				}
			} catch (IllegalAccessException ex) {
				System.out.println("Error " + ex.getMessage());
			}
		}
		sql="insert into " + table + "(" + lstField + ")values(" + lstValue + ");";
		return sql;
	}
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

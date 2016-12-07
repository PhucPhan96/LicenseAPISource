package nfc.serviceImpl.common;

import java.lang.reflect.Field;
import java.util.Collection;

import com.google.gson.Gson;

public class Utils {
	public static String appId="e6271952-d4b9-4ed3-b83b-63a56d47a713";
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
}

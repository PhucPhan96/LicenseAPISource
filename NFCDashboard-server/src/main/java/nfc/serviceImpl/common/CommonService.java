package nfc.serviceImpl.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import nfc.model.Code;
import nfc.service.common.ICommonService;

public class CommonService implements ICommonService {
	public Object createObject(String className,Map<String, String> map){
		try {
			Class c=Class.forName(className);
			Object instance = c.newInstance();
			for (Map.Entry<String, String> entry : map.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			    Field field = c.getDeclaredField(key);
	            field.setAccessible(true);
	            field.set(instance,value);
	            field.setAccessible(false);
			}
			return instance;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

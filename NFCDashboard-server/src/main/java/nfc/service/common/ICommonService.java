package nfc.service.common;

import java.util.Map;

import nfc.model.Code;

public interface ICommonService {
	Object createObject(String className,Map<String, String> map);
}

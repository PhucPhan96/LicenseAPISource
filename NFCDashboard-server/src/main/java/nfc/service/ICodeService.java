package nfc.service;

import java.util.List;

import nfc.model.Code;

public interface ICodeService {
	List<Code> getListCode(String groupCode);
}

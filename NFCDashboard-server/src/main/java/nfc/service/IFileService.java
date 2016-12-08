package nfc.service;

import java.util.List;

import nfc.model.AttachFile;

public interface IFileService {
	boolean saveFile(AttachFile file);
	AttachFile getAttachFile(int fileId);
}

package nfc.model.ViewModel;

import java.util.List;

import nfc.model.AttachFile;
import nfc.model.ThreadModel;
import nfc.model.User;

public class ThreadView {
	private ThreadModel thread;
	private String username;
	private List<AttachFile> lstAttachFile;
	public ThreadModel getThread() {
		return thread;
	}
	public void setThread(ThreadModel thread) {
		this.thread = thread;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<AttachFile> getLstAttachFile() {
		return lstAttachFile;
	}
	public void setLstAttachFile(List<AttachFile> lstAttachFile) {
		this.lstAttachFile = lstAttachFile;
	}
}

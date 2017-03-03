package nfc.service;

import java.util.List;

import nfc.model.ThreadModel;
import nfc.model.ViewModel.ThreadView;

public interface IThreadService {
	List<ThreadView> getListThreadView(String username);
	List<ThreadModel> getListThread(String username);
}

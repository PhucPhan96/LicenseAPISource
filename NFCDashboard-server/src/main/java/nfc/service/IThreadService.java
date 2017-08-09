package nfc.service;

import java.util.List;

import nfc.model.ThreadModel;
import nfc.model.ViewModel.ThreadView;

public interface IThreadService {
	List<ThreadView> getListThreadView(String username);
	List<ThreadModel> getListThread(String username);
        nfc.model.Thread insertThread(nfc.model.Thread thread);
        boolean updateThread(nfc.model.Thread thread);
        boolean deleteThread(String threadId);
}

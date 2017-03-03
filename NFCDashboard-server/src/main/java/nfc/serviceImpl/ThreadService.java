package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.AttachFile;
import nfc.model.ThreadModel;
import nfc.model.User;
import nfc.model.ViewModel.ThreadView;
import nfc.service.IThreadService;
import nfc.service.IUserService;

public class ThreadService implements IThreadService{
	@Autowired
	private IUserService userDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<ThreadView> getListThreadView(String username){
		List<ThreadView> listThreadView = new ArrayList<ThreadView>();
		setupListThreadView(listThreadView, username);
		System.out.println("Size of list thread " + listThreadView.size());
		return listThreadView;
	}
	private void setupListThreadView(List<ThreadView> listThreadView, String username){
		List<ThreadModel> lstThread = getListThread(username);
		for(ThreadModel thread: lstThread){
			listThreadView.add(getThreadView(thread));
		}
	}
	private ThreadView getThreadView(ThreadModel thread){
		return setupThreadView(thread);
	}
	private ThreadView setupThreadView(ThreadModel thread){
		ThreadView threadView = new ThreadView();
		threadView.setThread(thread);
		threadView.setUsername(getUserNameOfThread(thread.getWriter_id()));
		threadView.setLstAttachFile(getListImageOfThread(thread.getThread_id()));
		return threadView;
	}
	
	public List<AttachFile> getListImageOfThread(int threadId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"select f.* from fg_files f inner join fg_thread_imgs ti on f.file_id = ti.img_id where ti.thread_id = " + threadId)
				.addEntity(AttachFile.class);
		List<AttachFile> listAttachFile = query.list();
		trans.commit();
		return listAttachFile;
	}
	public List<ThreadModel> getListThread(String username){
		User user = userDAO.findUserByUserName(username);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Query query = session.createSQLQuery(
				"CALL GetListThreadParentOfStore(:userId)")
				.addEntity(ThreadModel.class)
				.setParameter("userId", user.getUser_id());
		List<ThreadModel> listThread = query.list();
		trans.commit();
		return listThread;
	}
	private String getUserNameOfThread(String userId){
		User user = userDAO.getUser(userId);
		return user.getFirst_name() + " " + user.getMiddle_name() + " " + user.getLast_name();
	}
}

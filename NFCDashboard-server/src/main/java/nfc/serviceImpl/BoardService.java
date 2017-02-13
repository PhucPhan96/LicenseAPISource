package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.AttachFile;
import nfc.model.Board;
import nfc.model.Role;
import nfc.model.User;
import nfc.model.Thread;
import nfc.model.ThreadImg;
import nfc.service.IBoardService;
import nfc.service.IFileService;

public class BoardService implements IBoardService{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private IFileService fileDAO;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Override
	public boolean insertBoard(Board board) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			/*if(cate.getCate_img_id() == 0)
				cate.setCate_img_id(null);*/
			session.save(board);
			trans.commit();
			return true;			
		}
		catch(Exception ex)
		{
			System.out.println("Error " + ex.getMessage());
			trans.rollback();
			return false;
		}
	}
	public List<Board> getListBoard(String userName)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		String userId = getUserIdfromUserName(userName, session);
		Criteria criteria = session.createCriteria(Board.class);
		criteria.add(Restrictions.eq("owner_id",userId));
		List<Board> list = (List<Board>) criteria.list();
		for(Board board:list)
		{
			board.setOwner_name(userName);
		}
		trans.commit();
		return list;
	}
	public String getUserIdfromUserName(String userName, Session session)
	{
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("user_name",userName));
		List<User> list = (List<User>) criteria.list();		
		String userId = list.get(0).getUser_id();
		System.out.println(userId);
		return userId; 
	}
	public List<Thread> getListThread(int board_id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Thread.class);
		criteria.add(Restrictions.eq("board_id",board_id));
		List<Thread> list = (List<Thread>) criteria.list();
		for(Thread thread:list)
		{			
			Criteria criteria1 = session.createCriteria(ThreadImg.class);
			criteria1.add(Restrictions.eq("thread_id",thread.getThread_id()));
			List<ThreadImg> listThImg = (List<ThreadImg>)criteria1.list();
//			System.out.println(listThImg.get(0).getImg_id());
//			System.out.println(listThImg.get(1).getImg_id());
			List<AttachFile> listAtt = new ArrayList<AttachFile>();
			for(ThreadImg item : listThImg)
			{
				listAtt.add(fileDAO.getAttachFileWithSession(item.getImg_id(),session));				
			}
			if(listAtt.size()>0)
			{
				thread.setAttachFile(listAtt);
			}
		}
		
		trans.commit();
		return list;
	}
	
	public boolean deleteThread(String threadId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{	
			String deleteQuery = "delete from fg_thread_imgs where thread_id = " + threadId;
			Query query = session.createSQLQuery(deleteQuery);
		    query.executeUpdate();
		    
		    String deleteThread = "delete from fg_threads where thread_id = " + threadId;
			Query queryThread = session.createSQLQuery(deleteThread);
			queryThread.executeUpdate();
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		} 
	}
	public boolean deleteBoard(int boardId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{	Criteria criteria = session.createCriteria(Thread.class);
			criteria.add(Restrictions.eq("board_id",boardId));
			List<Thread> list = (List<Thread>) criteria.list();
			for(Thread thread:list)
			{
				String deleteQuery = "delete from fg_thread_imgs where thread_id = " + thread.getThread_id();
				Query query = session.createSQLQuery(deleteQuery);
			    query.executeUpdate();
			    
			    String deleteThread = "delete from fg_threads where thread_id = " + thread.getThread_id();
				Query queryThread = session.createSQLQuery(deleteThread);
				queryThread.executeUpdate();
			}
			String deleteBoard = "delete from fg_boards where board_id = " + boardId;
			Query queryBoard = session.createSQLQuery(deleteBoard);
			queryBoard.executeUpdate();
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		} 
	}

}
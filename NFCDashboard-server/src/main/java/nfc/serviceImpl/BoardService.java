package nfc.serviceImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Board;
import nfc.model.User;
import nfc.model.Thread;
import nfc.service.IBoardService;

public class BoardService implements IBoardService{
	@Autowired
	private SessionFactory sessionFactory;
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
		//criteria.add(Restrictions.eq("board_id",board_id));
		List<Thread> list = (List<Thread>) criteria.list();
		trans.commit();
		return list;
	}

}

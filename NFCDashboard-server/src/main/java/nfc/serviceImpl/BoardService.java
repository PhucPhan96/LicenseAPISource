package nfc.serviceImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Board;
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

}

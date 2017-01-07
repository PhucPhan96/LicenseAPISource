package nfc.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Category;
import nfc.model.Program;
import nfc.model.User;
import nfc.service.IProgramService;
import nfc.service.ISupplierService;
import nfc.serviceImpl.common.Utils;

@Transactional
public class ProgramService implements IProgramService {
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Program> getListProgram(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Program.class);		
		List<Program> list = (List<Program>)criteria.list();			
		trans.commit();
		System.out.println("count:" + list.size());
		return list;		
	}
	public boolean insertProgram(Program prog)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(prog);
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

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
		criteria.add(Restrictions.eq("use_yn", "Yes"));
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
	public boolean deleteProgram(String ProgId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{	String deleteQueryChild = "update fg_programs set use_yn ='No' where parent_prog_id = '" + ProgId +"'";
			String deleteQuery = "update fg_programs set use_yn ='No' where program_id = '" + ProgId +"'";
			Query query = session.createSQLQuery(deleteQueryChild);
		    query.executeUpdate();
		    
		    query = session.createSQLQuery(deleteQuery);
		    query.executeUpdate();
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
//	public boolean deleteProgram(String ProgId)
//	{
//		Session session = this.sessionFactory.getCurrentSession();
//		Transaction trans = session.beginTransaction();
//		try
//		{	String deleteQueryChild = "delete from fg_programs where parent_prog_id = '" + ProgId+"'";
//			String deleteQuery = "delete from fg_programs where program_id = '" + ProgId+"'";
//			Query query = session.createSQLQuery(deleteQueryChild);
//		    query.executeUpdate();
//		    
//		    query = session.createSQLQuery(deleteQuery);
//		    query.executeUpdate();
//		    trans.commit();
//		    return true;
//		}
//		catch(Exception ex)
//		{
//			System.out.println("Error " + ex.getMessage());
//			trans.rollback();
//			return false;
//		}
//	}
	public boolean editProgram(Program prog)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{	
			String editQuery = "update fg_programs set prog_name ='"+ prog.getProg_name()+"' where program_id = '" + prog.getProgram_id()+"'";
			Query query = session.createSQLQuery(editQuery);
		    query.executeUpdate();
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

package nfc.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.hql.internal.ast.SqlASTFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Category;
import nfc.model.Program;
import nfc.model.ProgramRole;
import nfc.model.User;
import nfc.model.ViewModel.ProgramRoleSubmit;
//import nfc.model.ViewModel.ProgramRoleView;
import nfc.service.IProgramService;
import nfc.service.ISupplierService;
import nfc.serviceImpl.common.Utils;

@Transactional
public class ProgramService implements IProgramService {
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Program> getListProgram(String username){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		
		String sql = "SELECT * FROM fg_programs WHERE program_id IN (SELECT distinct program_id FROM fg_program_role WHERE role_id IN "
				+ "(SELECT role_id FROM fg_user_roles WHERE user_id IN(SELECT user_id  FROM fg_users WHERE user_name  ='"+username+"'))) "
				+ "and use_yn='Yes' UNION SELECT * FROM fg_programs WHERE parent_prog_id !='0' and use_yn='Yes' and parent_prog_id IN"
				+ " (SELECT program_id FROM fg_programs WHERE program_id IN (SELECT distinct program_id FROM fg_program_role WHERE role_id IN"
				+ "(SELECT role_id FROM fg_user_roles WHERE user_id IN(SELECT user_id  FROM fg_users WHERE user_name  ='"+username+"'))) and use_yn='Yes')";
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Program.class);
		List<Program> list = query.list();			
		trans.commit();
		return list;		
	}
	public List<Program> getListProgramFull(){
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
	public List<ProgramRole> getListProgramRolebyRoleId(int roleId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(ProgramRole.class);	
		criteria.add(Restrictions.eq("role_id", roleId));
		List<ProgramRole> list = (List<ProgramRole>)criteria.list();			
		trans.commit();
		System.out.println("count:" + list.size());
		return list;	
	}
	public boolean SaveProgRole(ProgramRoleSubmit progRole)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{	
			String deleteQuery = "delete from fg_program_role where role_id = '" + progRole.getRole_id()+"'";
			Query query = session.createSQLQuery(deleteQuery);
		    query.executeUpdate();
		    if(progRole.getListProgRole().size()>0)
		    {
		    	for(ProgramRole item:progRole.getListProgRole())
		    	{
		    		String addQuery ="INSERT INTO fg_program_role VALUES ('"+item.getProgram_id()+"','"+item.getApp_id()+"',"+item.getRole_id()+")";
		    		query = session.createSQLQuery(addQuery);
		    		query.executeUpdate();
		    	}
		    	
		    }
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

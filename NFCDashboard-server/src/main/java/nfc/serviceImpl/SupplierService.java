package nfc.serviceImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import nfc.model.Supplier;
import nfc.model.User;
import nfc.service.ISupplierService;;

@Transactional
public class SupplierService implements ISupplierService {
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Supplier> getListSupplier(){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Supplier.class);
		return (List<Supplier>) criteria.list();
	}
	public boolean updateSupplier(Supplier supplier){
		try
		{
			Session session = this.sessionFactory.getCurrentSession();
			session.update(supplier);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	public boolean insertSupplier(Supplier supplier){
		try
		{
			Session session = this.sessionFactory.getCurrentSession();
			session.save(supplier);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	public Supplier getSupplier(String suppl_id){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Supplier.class);
		criteria.add(Restrictions.eq("suppl_id", Integer.parseInt(suppl_id)));
		return (Supplier) criteria.uniqueResult();
	}
}

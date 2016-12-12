package nfc.serviceImpl;

import java.util.List;

import nfc.model.Category;
import nfc.service.ICategoryService;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryService implements ICategoryService{
	@Autowired
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Category> getListCategory() {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Category.class);
		List<Category> list = (List<Category>)criteria.list();
		trans.commit();
		return list;		
	}
	public boolean insertCategory(Category cate)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(cate);
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
	public boolean updateCategory(Category cate)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(cate);
			trans.commit();
			return true;			
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	public Category getCategory(String cateID) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Category.class);
		criteria.add(Restrictions.eq("cate_id", Integer.parseInt(cateID)));
		Category cate = (Category)criteria.uniqueResult();
		trans.commit();
		return cate;
		
	}
	public boolean deleteCategory(String cateID) {
		Category cate = getCategory(cateID);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.delete(cate);
			trans.commit();
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			trans.rollback();
			return false;
		}
		
	}
	public List<Category> getListCategoryFilterType(String type) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Category.class);
		criteria.add(Restrictions.eq("cate_type", type));
		List<Category> list = (List<Category>)criteria.list();
		trans.commit();
		return list;
	}

}

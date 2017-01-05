package nfc.serviceImpl;

import java.io.Console;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import nfc.model.Category;
import nfc.model.Code;
import nfc.model.User;
import nfc.model.ViewModel.CategoryView;
import nfc.service.ICategoryService;
import nfc.service.IFileService;
import nfc.service.IRoleService;
import nfc.service.IUserService;
import nfc.service.common.ICommonService;
import nfc.serviceImpl.common.Utils;

import nfc.model.AttachFile;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryService implements ICategoryService{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private IFileService fileDAO;
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
	public List<CategoryView> getListCategoryView(String type){
		List<CategoryView> lstCategoryView = new ArrayList<CategoryView>();
		List<Category> lstCategory = getListCategoryFilterType(type);
		for(Category cate: lstCategory){
			CategoryView cateView = new CategoryView();
			cateView.setCategory(cate);
			cateView.setAttachFile(fileDAO.getAttachFile(cate.getCate_img_id()));
			lstCategoryView.add(cateView);
		}
		return lstCategoryView;
	}
}

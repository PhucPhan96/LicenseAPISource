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
import nfc.model.Product;
import nfc.model.SupplierCategories;
import nfc.model.SupplierWork;
import nfc.model.User;
import nfc.model.ViewModel.CategoryView;
import nfc.model.ViewModel.SupplierProductView;
import nfc.service.ICategoryService;
import nfc.service.IFileService;
import nfc.service.IProductService;
import nfc.service.IRoleService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.service.common.ICommonService;
import nfc.serviceImpl.common.Utils;

import nfc.model.AttachFile;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
	@Autowired
	private ISupplierService supplierDAO;
	@Autowired
	private ICategoryService categoryDAO;
	@Autowired
	private IProductService productDAO;
	@Autowired
	private IUserService userDAO;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Category> getListCategory(String username) {
		User user = userDAO.findUserByUserName(username);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		List<Category> result;
		try{
			Query query = session.createSQLQuery(
					"CALL GetListCategory(:userid)")
					.addEntity(Category.class)
					.setParameter("userid", user.getUser_id());
			result = query.list();
		}
		catch(Exception ex){
			result = new ArrayList<Category>();
		}
		trans.commit();
		return result;		
	}
	public boolean insertCategory(Category cate)
	{
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			if(cate.getCate_img_id() == 0)
				cate.setCate_img_id(null);
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
	private void deleteReferenceOfCategory(Session session, int cateId, String table)
	{
		String deleteQuery = "delete from "+table+" where cate_id = " + cateId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	public boolean deleteCategory(String cateID) {
		Category cate = getCategory(cateID);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			deleteReferenceOfCategory(session,Integer.parseInt(cateID), "fg_product_categories" );
			//deleteReferenceOfCategory(session,Integer.parseInt(cateID), "fg_products" );
			deleteReferenceOfCategory(session,Integer.parseInt(cateID), "fg_supplier_categories" );
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
			if(cate.getCate_img_id() != null)
				cateView.setAttachFile(fileDAO.getAttachFile(cate.getCate_img_id()));
			lstCategoryView.add(cateView);
		}
		return lstCategoryView;
	}
	
	private List<Category> getListCategoryFromSupplier(int supplierId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		String sql = "SELECT * FROM fg_categories c INNER JOIN fg_product_categories pc ON c.cate_id = pc.cate_id INNER JOIN fg_products p ON pc.prod_id = p.prod_id WHERE p.suppl_id = "+supplierId;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Category.class);
		List results = query.list();
		trans.commit();
		return results;
	}
	private List<Product> getListProductOfCategoryProduct(int cate){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		String sql = "SELECT * FROM fg_products p INNER JOIN fg_product_categories pc ON p.prod_id = pc.prod_id WHERE pc.cate_id = "+cate;
		SQLQuery query = session.createSQLQuery(sql);
		query.addEntity(Product.class);
		List results = query.list();
		trans.commit();
		return results;
	}
	
	@Override
	public List<SupplierProductView> getListProductOfCategory(int supplierId) {
		List<SupplierProductView> lstSupplierProductView = new ArrayList<SupplierProductView>();
		
		
		List<Category> categoies = getListCategoryFromSupplier(supplierId);
		for(Category category: categoies)
		{
			SupplierProductView supplierProductView = new SupplierProductView();
			supplierProductView.setCategory(category);
			supplierProductView.setProducts(getListProductOfCategoryProduct(category.getCate_id()));
			lstSupplierProductView.add(supplierProductView);
		}
		/*List<Product> lstProdduct = productDAO.getListProduct(supplierId);
		
		
		List<SupplierCategories> lstSupplierCategory = supplierDAO.getListSupplierCategory(supplierId);
		for(SupplierCategories supplCate: lstSupplierCategory)
		{
			SupplierProductView supplierProductView = new SupplierProductView();
			supplierProductView.setCategory(categoryDAO.getCategory(supplCate.getCate_id()+""));
			supplierProductView.setProducts(productDAO.getListProductOfCategory(supplCate.getCate_id(), supplierId));
			lstSupplierProductView.add(supplierProductView);
		}*/
		return lstSupplierProductView;
	}
}

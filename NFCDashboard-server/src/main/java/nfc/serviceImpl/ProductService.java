package nfc.serviceImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.AttachFile;
import nfc.model.Code;
import nfc.model.Product;
import nfc.model.ProductCategory;
import nfc.model.ProductImage;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.service.IProductService;
import nfc.service.common.ICommonService;
public class ProductService implements IProductService{
	@Autowired
	private ICommonService commonDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Product> getListProduct(int supplId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class, "product")
				.createAlias("product.attachFiles", "attachFile", Criteria.INNER_JOIN)
				.createAlias("product.category","category",Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("suppl_id",supplId));
		List<Product>  products = (List<Product>) criteria.list();
		trans.commit();
		return products;
	}
	public Product getProduct(int productId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class, "product")
				.createAlias("product.attachFiles", "attachFile", Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("prod_id",productId));
		Product product = (Product) criteria.uniqueResult();
		trans.commit();
		return product;
	}
	private void deleteImagesOfProduct(Session session, int productId)
	{
		String deleteQuery = "delete from fg_prod_imgs where prod_id = " + productId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	private void deleteCategoryProduct(Session session, int productId)
	{
		String deleteQuery = "delete from fg_product_categories where prod_id = " + productId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	private void insertProductImage(Session session, Product product){
		for(AttachFile attachFile : product.getAttachFiles())
        {
        	ProductImage proImg = new ProductImage();
        	proImg.setImg_id(attachFile.getFile_id());
        	proImg.setImg_name(attachFile.getFile_name());
        	proImg.setProd_id(product.getProd_id());
        	proImg.setImg_type(attachFile.getFile_name().split("\\.")[1]);
        	session.save(proImg);
        }
	}
	private void insertProductCategory(Session session, Product product){
		ProductCategory proCate = new ProductCategory();
		proCate.setProd_id(product.getProd_id());
		proCate.setCate_id(product.getCategory().getCate_id());
		session.save(proCate);
	}
	public boolean insertProduct(Product product) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(product);
			//deleteImagesOfProduct(session, product.getProd_id());
	        insertProductImage(session, product);
	        //deleteCategoryProduct(session, product.getProd_id());
	        insertProductCategory(session, product);
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
	public List<Product> getProduct(String prodId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("suppl_id", Integer.parseInt(prodId)));
		//Product product = (Product) criteria.uniqueResult();
		List<Product> product = (List<Product>) criteria.list();
		trans.commit();
		return product;
	}
	public Product getProducts(String productId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("prod_id", Integer.parseInt(productId)));
		Product product = (Product) criteria.uniqueResult();
		trans.commit();
		return product;
	}
	
	
	
}

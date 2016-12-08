package nfc.serviceImpl;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Code;
import nfc.model.Product;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.service.IProductService;
import nfc.service.common.ICommonService;
public class ProductService implements IProductService {
	@Autowired
	private ICommonService commonDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Product> getListProduct(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		List<Product> list = (List<Product>) criteria.list();
		trans.commit();
		return list;
	}
	public boolean insertProduct(Product product){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int roleIdDesc = 0;
			Serializable ser = session.save(product);
	        if (ser != null) {
	        	roleIdDesc = (Integer) ser;
	        }
			Map<String, String> map = new HashMap<String, String>();
			map.put("group_code", "0003");
			map.put("sub_code", roleIdDesc+"");
			Code code = (Code) commonDAO.createObject("nfc.model.Code", map);
			session.save(code);
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
	public Product getProduct(String prodId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Product.class);
		criteria.add(Restrictions.eq("prod_id", Integer.parseInt(prodId)));
		Product product = (Product) criteria.uniqueResult();
		trans.commit();
		return product;
	}
}

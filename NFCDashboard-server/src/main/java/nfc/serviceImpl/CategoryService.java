package nfc.serviceImpl;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import nfc.model.Category;
import nfc.model.Code;
import nfc.model.User;
import nfc.service.ICategoryService;
import nfc.service.IRoleService;
import nfc.service.IUserService;
import nfc.service.common.ICommonService;
import nfc.serviceImpl.common.Utils;

import org.hibernate.Criteria;
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

package nfc.serviceImpl;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import nfc.model.Code;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.service.ISupplierService;
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

public class SupplierService implements ISupplierService {
	@Autowired
	//private ICommonService commonDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Supplier> getListSupplier(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Supplier.class);
		List<Supplier> list = (List<Supplier>) criteria.list();
		trans.commit();
		return list;
	}
}

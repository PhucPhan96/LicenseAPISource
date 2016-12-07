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
import nfc.model.SupplierWork;
import nfc.service.ISupplierService;
import nfc.service.ISupplierWorkService;
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

public class SupplierWorkService implements ISupplierWorkService {
	@Autowired
	private ICommonService commonDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<SupplierWork> getListSupplierWork(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(SupplierWork.class);
		List<SupplierWork> list = (List<SupplierWork>) criteria.list();
		trans.commit();
		return list;
	}
	public boolean insertSupplierWork(SupplierWork supplierwork){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int subcode = 0;
			supplierwork.setSuppl_id(1);
			supplierwork.setIs_active(1);
			Serializable ser = session.save(supplierwork);
	        if (ser != null) {
	        	subcode = (Integer) ser;
	        }
			Map<String, String> map = new HashMap<String, String>();
			map.put("group_code", "0003");
			map.put("sub_code", subcode+"");
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
}

package nfc.serviceImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import nfc.model.Code;
import nfc.model.Group;
import nfc.service.ICodeService;

public class CodeService implements ICodeService{
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Code> getListCode(String groupCode) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Code.class);
		criteria.add(Restrictions.eq("group_code", groupCode));
		List<Code> list = (List<Code>) criteria.list();
		trans.commit();
		return list;
	}
	public Code getCode(String groupCode, String subCode){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Code.class);
		criteria.add(Restrictions.eq("group_code", groupCode));
		criteria.add(Restrictions.eq("sub_code", subCode));
		Code code = (Code) criteria.uniqueResult();
		trans.commit();
		return code;
	}
	
}

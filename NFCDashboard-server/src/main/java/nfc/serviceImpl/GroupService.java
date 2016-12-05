package nfc.serviceImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import nfc.model.Group;
import nfc.service.IGroupService;

public class GroupService implements IGroupService{
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Group> getListGroup() {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Group.class);
		List<Group> list = (List<Group>) criteria.list();
		trans.commit();
		return list;
	}
}

package nfc.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Role;
import nfc.model.User;
import nfc.model.UserRole;
import nfc.service.IRoleService;
import nfc.service.IUserService;

@Transactional
public class UserService implements IUserService {
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<User> getListUser(){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		return (List<User>) criteria.list();
	}
	public boolean updateUser(User user){
		try
		{
			Session session = this.sessionFactory.getCurrentSession();
			session.update(user);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	public boolean insertUser(User user){
		try
		{
			Session session = this.sessionFactory.getCurrentSession();
			session.save(user);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	public boolean deleteUser(String userId){
		try
		{
			Session session = this.sessionFactory.getCurrentSession();
			User user = getUser(userId);
			session.delete(user);
			return true;
		}
		catch(Exception ex)
		{
			return false;
		} 
	}
	public User getUser(String userId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", Integer.parseInt(userId)));
		User user =  (User) criteria.uniqueResult(); 
		trans.commit();
		return user;
	}
	public User findUserByUserName(String username) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("user_name", username));
		User user =  (User) criteria.uniqueResult(); 
		trans.commit();
		return user;
	}
	public List<UserRole> getListUserRole(String userId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<UserRole> userRoles =  (List<UserRole>) criteria.list(); 
		trans.commit();
		return userRoles;
	}
}

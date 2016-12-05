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
import nfc.model.User;
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

public class RoleService implements IRoleService {
	@Autowired
	private ICommonService commonDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<Role> getListRole(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Role.class);
		List<Role> list = (List<Role>) criteria.list();
		trans.commit();
		return list;
	}
	public boolean insertRole(Role role){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int roleIdDesc = 0;
			Serializable ser = session.save(role);
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
	public Role getRole(String roleId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Role.class);
		criteria.add(Restrictions.eq("role_id", Integer.parseInt(roleId)));
		Role role = (Role) criteria.uniqueResult();
		trans.commit();
		return role;
	}
	public boolean updateRole(Role role){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(role);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	public boolean deleteRole(String roleId){
		Role role = getRole(roleId);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("group_code", "0003");
			map.put("sub_code", roleId+"");
			Code code = (Code) commonDAO.createObject("nfc.model.Code", map);
			session.delete(code);
			session.delete(role);
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		} 
	}
}

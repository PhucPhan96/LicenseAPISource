package nfc.serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Address;
import nfc.model.Category;
import nfc.model.Role;
import nfc.model.SupplierAddress;
import nfc.model.SupplierUser;
import nfc.model.User;
import nfc.model.UserAddress;
import nfc.model.UserRole;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.UserAddressView;
import nfc.service.IRoleService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;

@Transactional
public class UserService implements IUserService {
	@Autowired
	private ISupplierService supplDAO;
	private SessionFactory sessionFactory;
	@Autowired
	private IRoleService roleServiceDao;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public List<User> getListUser(){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(User.class);		
		List<User> list = (List<User>)criteria.list();			
		trans.commit();
		System.out.println("count:" + list.size());
		return list;		
	}
	public boolean updateUser(User user){	
		System.out.println("vao dc update");
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(user);
			updateUserSupplier(session,user);
			deleteAddressOfUser(session, user.getUser_id());
			for(UserAddressView addrView: user.getLstuserAddress())
			{
				int addressIdDesc = 0;
				addrView.getAddressOfUser().setApp_id(Utils.appId);
				Serializable serAdd = session.save(addrView.getAddressOfUser());
		        if (serAdd != null) {
		        	addressIdDesc = (Integer) serAdd;
		        	System.out.println("add ID: "+addressIdDesc);
		        }
				session.save(addrView.getAddressOfUser());
				UserAddress userAddr = new UserAddress();
				userAddr.setAddr_id(addressIdDesc);
				userAddr.setApp_id(Utils.appId);
				userAddr.setUser_id(user.getUser_id());
				userAddr.setIs_deliver(addrView.isIs_deliver());
				userAddr.setIs_main(addrView.isIs_main());
				session.save(userAddr);
			}
			deleteRoleOfUser(session, user.getUser_id());
			insertUserRole(session, user);
			trans.commit();
			return true;			
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	public boolean insertUser(User user){
		
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(user);
			insertUserRole(session, user);
			insertUserSupplier(session,user);
			//save supplier address
			System.out.println("con save address");
			System.out.println(user.getLstuserAddress().size());
			
			for(UserAddressView addrView: user.getLstuserAddress())
			{
				int addressIdDesc = 0;
				addrView.getAddressOfUser().setApp_id(Utils.appId);
				Serializable serAdd = session.save(addrView.getAddressOfUser());
		        if (serAdd != null) {
		        	addressIdDesc = (Integer) serAdd;
		        	System.out.println("add ID: "+addressIdDesc);
		        }
				session.save(addrView.getAddressOfUser());
				UserAddress userAddr = new UserAddress();
				userAddr.setAddr_id(addressIdDesc);
				userAddr.setApp_id(Utils.appId);
				userAddr.setUser_id(user.getUser_id());
				userAddr.setIs_deliver(addrView.isIs_deliver());
				userAddr.setIs_main(addrView.isIs_main());
				session.save(userAddr);
			}			
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
	private void insertUserRole(Session session, User user)
	{
		for(Role r :user.getLstRoles())
		{
			UserRole userRole = new UserRole();
			userRole.setUser_id(user.getUser_id());
			userRole.setApp_id(Utils.appId);
			userRole.setRole_id(r.getRole_id());
			session.save(userRole);
		}
		
	}
	private void insertUserSupplier(Session session, User user)
	{
		SupplierUser userSuppl = new SupplierUser();
		userSuppl.setUser_id(user.getUser_id());
		userSuppl.setApp_id(user.getApp_id());
		userSuppl.setSuppl_id(user.getSuppl_id());
		session.save(userSuppl);
	}
	private void updateUserSupplier(Session session, User user)
	{
		String updateQuery = "update fg_supplier_users set suppl_id ="+ user.getSuppl_id()+" where user_id = '" + user.getUser_id()+"'";
		Query query = session.createSQLQuery(updateQuery);
	    query.executeUpdate();
	}
	public boolean deleteUser(String userID) {
		System.out.print("Vao nay roi " + userID);
		User user = getUser(userID);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.delete(user);
			deleteAddressOfUser(session, userID);
			deleteSupplierOfUser(session, userID);
			trans.commit();
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			trans.rollback();
			return false;
		}
		
	}
	public User getUser(String userId){
		List<SupplierUser> supplUser = supplDAO.getListSupplierUserId(userId);
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("user_id", userId));
		User user =  (User) criteria.uniqueResult(); 
//		set SupplierUser
		if(supplUser.size()>0)
		{
			user.setSuppl_id(supplUser.get(0).getSuppl_id());
		}
//		 set Address user
		List<UserAddress> userAddresses = getListUserAddress(session,userId);
		List<UserAddressView> userAddressViewLst = new ArrayList<UserAddressView>();
		for(UserAddress userAddr: userAddresses)
		{
			UserAddressView userAddrView = new UserAddressView();
			userAddrView.setAddressOfUser(getAddress(session,userAddr.getAddr_id()));
			userAddrView.setIs_deliver(userAddr.isIs_deliver());
			userAddrView.setIs_main(userAddr.isIs_main());
			userAddressViewLst.add(userAddrView);
		}
		user.setLstuserAddress(userAddressViewLst);
//		set role user
		List<UserRole> listUserRoles = getlstUserRolePrivate(session,userId);
		List<Role> listRole = new ArrayList<Role>();
		for(UserRole item : listUserRoles)
		{
			Role role = new Role();
			role = roleServiceDao.getRoleWithSeeion(session,String.valueOf(item.getRole_id()));
			System.out.println("get 1 dc phan tu");	
			System.out.println("id la:"+ role.getRole_id());	
			listRole.add(role);
		}
		user.setLstRoles(listRole);
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
		session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<UserRole> userRoles =  (List<UserRole>) criteria.list(); 
		trans.commit();
		return userRoles;
	}
	public List<UserRole> getlstUserRolePrivate(Session session, String userId) {
		session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<UserRole> userRoles =  (List<UserRole>) criteria.list(); 
		return userRoles;
	}
	private void deleteAddressOfUser(Session session, String userId)
	{
		String deleteQuery = "delete from fg_user_address where user_id = '" + userId+"'";
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	private void deleteRoleOfUser(Session session, String userId)
	{
		String deleteQuery = "delete from fg_user_roles where user_id = '" + userId+"'";
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	private void deleteSupplierOfUser(Session session, String userId)
	{
		String deleteQuery = "delete from fg_supplier_users where user_id = '" + userId+"'";
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	public List<UserAddress> getListUserAddress(Session session,String userId){
		//Session session = this.sessionFactory.getCurrentSession();
		//Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(UserAddress.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<UserAddress> list = (List<UserAddress>) criteria.list();
		//trans.commit();
		return list;
	}
	public Address getAddress(Session session, int addrId){		
		//Session session = this.sessionFactory.getCurrentSession();
		//Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Address.class);
		criteria.add(Restrictions.eq("addr_id", addrId));
		Address role = (Address) criteria.uniqueResult();
		//trans.commit();
		return role;
	}
	public boolean ChangPasswordUser(String userId,String pass,String passSalt){	
		System.out.println("vao dc update");
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			String strQuery = "update fg_users set password='" + pass + "',password_salt='"+ passSalt +"' where user_id = '" + userId +"'";
			Query query = session.createSQLQuery(strQuery);
		    query.executeUpdate();			
			trans.commit();
			return true;			
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	public List<UserAddress> getListUserAddress(String userId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(UserAddress.class);
		criteria.add(Restrictions.eq("user_id", userId));
		List<UserAddress> lstUserAddress =  (List<UserAddress>) criteria.list(); 
		trans.commit();
		return lstUserAddress;
	}
}

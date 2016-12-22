package nfc.serviceImpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Category;
import nfc.model.OrderDetail;
import nfc.model.SupplierAddress;
import nfc.model.SupplierCategories;
import nfc.model.User;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.service.IOrderService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;

public class OrderService implements IOrderService{
	@Autowired
	private IUserService userDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private void insertOrderDetail(List<OrderDetail> lstOrderDetail, int orderIdDesc, Session session){
		for(OrderDetail orderDetail: lstOrderDetail)
		{
			orderDetail.setOrder_id(orderIdDesc);
			session.save(orderDetail);
		}
	}
	public boolean insertOrderView(OrderView orderView) {
		User user = userDAO.findUserByUserName(orderView.getUsername());
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int orderIdDesc = 0;
			orderView.getOrder().setUser_id(user.getUser_id());
			Serializable ser = session.save(orderView.getOrder());
	        if (ser != null) {
	        	orderIdDesc = (Integer) ser;
	        }
	        insertOrderDetail(orderView.getLstOrderDetail(), orderIdDesc,session);
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

	public boolean updateOrderView(OrderView orderView) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			
			session.update(orderView.getOrder());
			for(OrderDetail orderDetail: orderView.getLstOrderDetail()){
				session.update(orderDetail);
			}
			trans.commit();
			return true;
		}
		catch(Exception ex)
		{
			trans.rollback();
			return false;
		}
	}
	private void deleteReferenceOfSupplier(Session session, int orderId, String table)
	{
		String deleteQuery = "delete from "+table+" where order_id = " + orderId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	public boolean deleteOrderView(int orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			deleteReferenceOfSupplier(session, orderId, "fg_order_details");
			deleteReferenceOfSupplier(session, orderId, "fg_orders");
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

package nfc.serviceImpl;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.ObjectCodec;

import nfc.model.Category;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.Supplier;
import nfc.model.SupplierAddress;
import nfc.model.SupplierCategories;
import nfc.model.SupplierUser;
import nfc.model.User;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.service.IOrderService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;

public class OrderService implements IOrderService{
	@Autowired
	private IUserService userDAO;
	@Autowired
	private ISupplierService supplierDAO;
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
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		try
		{
			int orderIdDesc = 0;
			System.out.println("supplierId " + orderView.getOrder().getSuppl_id());
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
	private void deleteReferenceOfSupplier(Session session, long orderId, String table)
	{
		String deleteQuery = "delete from "+table+" where order_id = " + orderId;
		Query query = session.createSQLQuery(deleteQuery);
	    query.executeUpdate();
	}
	public boolean deleteOrderView(long orderId) {
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
	public List<OrderView> getListOrderViewForPos(String username) {
		// TODO Auto-generated method stub
		
		List<OrderView> lstOrderForPos = new ArrayList<OrderView>();
		User user = userDAO.findUserByUserName(username);
		List<SupplierUser> lstSupplierUser = supplierDAO.getListSupplierUser(username);
		int supplierId = 0;
		if(lstSupplierUser.size() > 0)
		{
			supplierId = lstSupplierUser.get(0).getSuppl_id();
		}
		
		List<Order> orders = getListOrderCurrentDay(supplierId);
		for(Order order: orders){
			OrderView orderView = new OrderView();
			orderView.setOrder(order);
			orderView.setLstOrderDetail(getListOrderDetail(order.getOrder_id()));
			User cusUser = userDAO.getUser(order.getUser_id());
			orderView.setCustomer_name(cusUser.getFirst_name() + " " + cusUser.getMiddle_name() + " " + cusUser.getLast_name());
			lstOrderForPos.add(orderView);
		}
		return lstOrderForPos;
	}
	public List<Order> getListOrder(int supplierId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.eq("suppl_id", supplierId));
		List<Order> orders = (List<Order>) criteria.list();
		trans.commit();
		return orders;
	}
	private List<Order> getListOrderCurrentDay(int supplierId)
	{
		java.util.Date date = new java.util.Date();
		Date dateSql = new Date(date.getYear(), date.getMonth(), date.getDate());
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.eq("suppl_id", supplierId));
		criteria.add(Restrictions.ge("order_date", dateSql));
		List<Order> orders = (List<Order>) criteria.list();
		trans.commit();
		return orders;
	}
	public List<OrderDetail> getListOrderDetail(int orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(OrderDetail.class);
		criteria.add(Restrictions.eq("order_id", orderId));
		List<OrderDetail> orderDetail = (List<OrderDetail>) criteria.list();
		trans.commit();
		return orderDetail;
	}
	public Order getOrder(int orderId){
		Session session = this.sessionFactory.getCurrentSession();
		Transaction trans = session.beginTransaction();
		Criteria criteria = session.createCriteria(Order.class);
		criteria.add(Restrictions.eq("order_id", orderId));
		Order order = (Order) criteria.uniqueResult();
		trans.commit();
		return order;
	}
}

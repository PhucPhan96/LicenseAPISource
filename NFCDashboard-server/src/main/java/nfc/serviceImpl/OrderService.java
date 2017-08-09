package nfc.serviceImpl;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.ObjectCodec;

import nfc.model.Category;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.PaymentOrderHistory;
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
    private long orderId = -1;
    private String dateGenerateOrderId;

    public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
    }

    private void insertOrderDetail(List<OrderDetail> lstOrderDetail, String orderIdDesc, Session session){
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
            String orderIdDesc = generateOrderId();
            orderView.getOrder().setOrder_id(orderIdDesc);
            session.save(orderView.getOrder());
            insertOrderDetail(orderView.getLstOrderDetail(), orderIdDesc, session);
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

    private void deleteReferenceOfSupplier(Session session, String orderId, String table)
    {
        String deleteQuery = "delete from "+table+" where order_id = '" + orderId + "'";
        Query query = session.createSQLQuery(deleteQuery);
        query.executeUpdate();
    }
    public boolean deleteOrderView(String orderId) {
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
    public String getOrderCount(int supplierId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("suppl_id", supplierId));
        criteria.setProjection(Projections.rowCount());
        List rowCount = criteria.list();
        String orderCount = rowCount.get(0).toString();
        trans.commit();
        return orderCount;
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
    private List<Order> getListOrderSearch(String dateFrom, String dateTo){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateF = null;
        try {
            dateF = formatter.parse(dateFrom);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date dateSqlFrom = new Date(dateF.getYear(), dateF.getMonth(), dateF.getDate());
        java.util.Date dateT=null;
        try {
            dateT = formatter.parse(dateTo);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date dateSqlTo = new Date(dateT.getYear(), dateT.getMonth(), dateT.getDate());
        System.out.println(dateSqlFrom);
        System.out.println(dateSqlTo);
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.between("order_date", dateSqlFrom, dateSqlTo));
        List<Order> orders = (List<Order>) criteria.list();
        trans.commit();
        return orders;
    }
    public List<OrderView> getListOrderViewSearch(String dateFrom, String dateTo){
        List<OrderView> lstOrderForPos = new ArrayList<OrderView>();
        List<Order> orders = getListOrderSearch(dateFrom,dateTo);
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
    public List<OrderDetail> getListOrderDetail(String orderId) {
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(OrderDetail.class);
        criteria.add(Restrictions.eq("order_id", orderId));
        List<OrderDetail> orderDetail = (List<OrderDetail>) criteria.list();
        trans.commit();
        return orderDetail;
    }
    public Order getOrder(String orderId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("order_id", orderId));
        Order order = (Order) criteria.uniqueResult();
        trans.commit();
        return order;
    }
        
        
    public Order getLastOrder(){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Order order = new Order();
        try{
            Query query = session.createSQLQuery("SELECT * FROM 82wafoodgo.fg_orders order by order_id desc limit 1")
                        .addEntity(Order.class);
            order = (Order) query.uniqueResult();
        }
        catch(Exception ex){

        }

        trans.commit();
        return order;
    }
    
    public String generateOrderId(){
        SimpleDateFormat df = new SimpleDateFormat("yymmdd");
        String orderIdGenerated = df.format(new java.util.Date());
        if(orderId == -1){
            Order lastOrder = getLastOrder();
            if(lastOrder != null && lastOrder.getOrder_id().substring(0, 6) == orderIdGenerated){
                orderId = Long.parseLong(lastOrder.getOrder_id().substring(6));
                dateGenerateOrderId = orderIdGenerated;
            }
            else{
                orderId = 0;
                dateGenerateOrderId = orderIdGenerated;
            }
            
        }
        else if(dateGenerateOrderId != orderIdGenerated){
            orderId = 0;
            dateGenerateOrderId = orderIdGenerated;
        }
        orderId = orderId + 1;
        if(orderId < 10){
            orderIdGenerated = orderIdGenerated + "000000" + orderId;
        }
        else if (orderId < 100){
            orderIdGenerated = orderIdGenerated + "00000" + orderId;    
        }
        else if (orderId < 1000){
            orderIdGenerated = orderIdGenerated + "0000" + orderId;    
        }
        else if (orderId < 10000){
            orderIdGenerated = orderIdGenerated + "000" + orderId;    
        }
        else if (orderId < 100000){
            orderIdGenerated = orderIdGenerated + "00" + orderId;    
        }
        else if (orderId < 1000000){
            orderIdGenerated = orderIdGenerated + "0" + orderId;    
        }
        else{
            orderIdGenerated = orderIdGenerated + orderId;    
        }
        return orderIdGenerated;
    }
    
    
    public boolean savePaymentOrderHistory(PaymentOrderHistory paymentOrderHistory){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            session.save(paymentOrderHistory);
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
    
    public boolean updateOrderStatus(String orderId, String status){  
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        try{
            String deleteQuery = "update fg_orders set order_status = '" + status + "' where order_id = '" + orderId + "'";
            Query query = session.createSQLQuery(deleteQuery);
            query.executeUpdate();
            trans.commit();
            return true;
        }
        catch(Exception ex){
            trans.rollback();
            return false;
        }
    }
}

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
import nfc.messages.filters.StatisticRequestFilter;

import nfc.model.Category;
import nfc.model.Customer;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.PaymentOrderHistory;
import nfc.model.Supplier;
import nfc.model.SupplierAddress;
import nfc.model.SupplierCategories;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.User;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.SupplierAddressView;
import nfc.model.ViewModel.UserAddressView;
import nfc.service.IOrderService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.Utils;
import org.hibernate.transform.Transformers;
import org.springframework.util.StringUtils;

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
            setupCustomerInformationForOrder(session, orderView);
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
    
    private void setupCustomerInformationForOrder(Session session, OrderView orderView){
         if(StringUtils.isEmpty(orderView.getOrder().getUser_id())){
            insertCustomer(session, orderView.getCustomer());
            orderView.getOrder().setCustomer_id(orderView.getCustomer().getCustomer_id());
        }
    }
    
    private void insertCustomer(Session session, Customer customer){
        if(!isCustomerExist(session, customer)){
            session.save(customer);
        }
    }
    
    private boolean isCustomerExist(Session session, Customer customer){
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.add(Restrictions.eq("customer_phone", customer.getCustomer_phone()));
        criteria.add(Restrictions.eq("customer_email", customer.getCustomer_email()));
        criteria.add(Restrictions.eq("customer_address", customer.getCustomer_address()));
        criteria.add(Restrictions.eq("customer_name", customer.getCustomer_name()));
        Customer cus = (Customer)criteria.uniqueResult();
        if(cus == null){
            return false;
        }
        return true;
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
            //orderView.setCustomer_name(cusUser.getFirst_name() + " " + cusUser.getMiddle_name() + " " + cusUser.getLast_name());
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
            //orderView.setCustomer_name(cusUser.getFirst_name() + " " + cusUser.getMiddle_name() + " " + cusUser.getLast_name());
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
        Order order = new Order();
        try{
            Criteria criteria = session.createCriteria(Order.class);
            criteria.add(Restrictions.eq("order_id", orderId));
            order = (Order) criteria.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return order;
    }
    /**
     * Lucas - Get List Order From SupplierID By Filter date, status (All Information)
     **/
    public List<Order> fGetListOrderByFilter(int[] suppliers, String fromDate, String toDate, String status) {
        List<Order> lstOrder = new ArrayList<Order>();
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        for (int supplId : suppliers) {
            System.out.println(supplId);
            String sqlQuery = "SELECT * FROM 82wafoodgo.fg_orders where suppl_id = '" + supplId + "' and order_status = '" + status
                    + "' and order_date >= '" + fromDate + "' and order_date <= '" + toDate + "';";
            List<Order> lstOrderTemp = new ArrayList<Order>();
            try {
                Query query = session.createSQLQuery(sqlQuery).addEntity(Order.class);;
                lstOrderTemp = (List<Order>) query.list();
            } catch (Exception ex) {
                System.out.println("Loi Ne");
                System.out.println(ex);
            }
            for (Order order: lstOrderTemp) {
                lstOrder.add(order);
            }
        }
        trans.commit();
        return lstOrder;
    }    
    public Order getLastOrder(){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Order order = new Order();
        try{
            Query query = session.createSQLQuery("SELECT * FROM 82wafoodgo.fg_orders order by order_id desc limit 1")
                          .addEntity(Order.class);
            order = (Order) query.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
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
    
    public PaymentOrderHistory getPaymentOrderHistory(String orderId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        PaymentOrderHistory paymentOrderHistory = new PaymentOrderHistory();
        try{
            Criteria criteria = session.createCriteria(PaymentOrderHistory.class);
            criteria.add(Restrictions.eq("order_id", orderId));
            paymentOrderHistory = (PaymentOrderHistory) criteria.list();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return paymentOrderHistory;
    }
    
    public List<Order> getListOrderAllStoreOfUser(String userId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select o.*, s.supplier_name from fg_orders o inner join fg_supplier_users su on o.suppl_id = su.suppl_id inner join fg_suppliers s on s.suppl_id = su.suppl_id where su.user_id='" + userId + "' and order_date >= current_date() order by o.order_date desc")
                                  .setResultTransformer(Transformers.aliasToBean(Order.class));
            orders = (List<Order>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return orders;
    }
    
   
    
    /*Lucas -  get list all order by supplierID
    */
    public List<OrderView> getListOrderBySupplierID(String username){
        List<OrderView> lstOrderView = new ArrayList<OrderView>();
        
        return lstOrderView;
    }
    
    public List<Order> getListOrderOfStatisticRequest(StatisticRequestFilter filter){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<Order> orders = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select * from fg_orders where find_in_set(suppl_id,'" + filter.getStoreIds() + "') and order_date >= '" + Utils.convertDateToString(filter.getDateFrom())+ "' and order_date <= '" + Utils.convertDateToString(filter.getDateTo())+ "'")
                                  .setResultTransformer(Transformers.aliasToBean(Order.class));
            orders = (List<Order>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return orders;
    }
    
}

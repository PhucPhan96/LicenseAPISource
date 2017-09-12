package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import nfc.model.Customer;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.PaymentOrderHistory;
import nfc.model.User;
import nfc.model.UserAddress;
import nfc.model.ViewModel.OrderDetailView;
import nfc.model.ViewModel.OrderDetailViewModel;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.PosDetailView;
import nfc.model.ViewModel.UserAddressView;
import nfc.service.IOrderService;
import nfc.service.IPosService;
import nfc.service.IProductService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.util.StringUtils;

public class PosService implements IPosService{
    
	@Autowired
	private IOrderService orderDAO;
	@Autowired
	private IProductService productDAO;
	@Autowired
	private IUserService userDAO;
	@Autowired
	private ISupplierService supplierDAO;
        
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public PosDetailView getPosDetailView(String orderId) {
            PosDetailView posDetailView = new PosDetailView();
            Order order = orderDAO.getOrder(orderId);
            if(order != null){
                posDetailView.setOrder(order);
                Customer customer = new Customer();
                if(StringUtils.isEmpty(order.getUser_id())){
                    //get from customer
                    customer = getCustomer(order.getCustomer_id());
                }
                else{
                    User user = userDAO.getUser(order.getUser_id());
                    customer.setCustomer_name(getNameOfUser(user));
                    customer.setCustomer_phone(user.getPhone_no());
                    customer.setCustomer_email(user.getEmail());
                    customer.setCustomer_address(getAddressOfUser(user));

                }
                posDetailView.setCustomer(customer);
                posDetailView.setListOrderDetailView(getListDetailViewModel(order.getOrder_id()));
                posDetailView.setPayment_code(getPaymentCode(order.getOrder_id()));

            }
            return posDetailView;
	}
        
        private String getPaymentCode(String orderId){
           PaymentOrderHistory orderHistory = orderDAO.getPaymentOrderHistory(orderId);
           if(orderHistory != null){
               return orderHistory.getPayment_code();
           }
           else{
               return "";
           }
        }
        
    
    private String getAddressOfUser(User user){
        String address = "";
        for(UserAddressView userAddressView: user.getLstuserAddress()){
            if(userAddressView.isIs_deliver()){
                address = userAddressView.getAddressOfUser().getAddress();
                return address;
            }
            address = userAddressView.getAddressOfUser().getAddress();
        }
        return address;
    }
    
    private String getNameOfUser(User user){
        return (StringUtils.isEmpty(user.getFirst_name())==true ? "" : user.getFirst_name()) 
                + " " + (StringUtils.isEmpty(user.getMiddle_name())==true ? "" : user.getMiddle_name())
                + " " + (StringUtils.isEmpty(user.getLast_name())==true ? "" : user.getLast_name());
    }
    
    private Customer getCustomer(String customerId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        Customer customer = new Customer();
        try{
            Criteria criteria = session.createCriteria(Customer.class);
            criteria.add(Restrictions.eq("customer_id", customerId));
            customer = (Customer) criteria.uniqueResult();
            trans.commit();
        }
        catch(Exception ex){
            trans.rollback();
        }
        return customer;
    }
    
    private List<OrderDetailViewModel> getListDetailViewModel(String orderId){
        Session session = this.sessionFactory.getCurrentSession();
        Transaction trans = session.beginTransaction();
        List<OrderDetailViewModel> listOrderDetail = new ArrayList<>();
        try{
            Query query = session.createSQLQuery("select od.*, p.prod_name from fg_order_details od inner join fg_products p on od.prod_id = p.prod_id where od.order_id = '" + orderId + "'")
                                  .setResultTransformer(Transformers.aliasToBean(OrderDetailViewModel.class));
            listOrderDetail = (List<OrderDetailViewModel>) query.list();
            trans.commit();
        }
        catch(Exception ex){
            System.err.println("error " + ex.getMessage());
            trans.rollback();
        }
        return listOrderDetail;
    }
        
}

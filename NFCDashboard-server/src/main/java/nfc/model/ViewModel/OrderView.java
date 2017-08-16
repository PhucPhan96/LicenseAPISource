package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;
import nfc.messages.base.PaymentRequestPacket;
import nfc.model.Customer;

import nfc.model.Order;
import nfc.model.OrderDetail;

public class OrderView {
    
    private Order order = new Order();
    private List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
    private PaymentRequestPacket payment_request;
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public List<OrderDetail> getLstOrderDetail() {
        return lstOrderDetail;
    }

    public void setLstOrderDetail(List<OrderDetail> lstOrderDetail) {
        this.lstOrderDetail = lstOrderDetail;
    }
        
    public PaymentRequestPacket getPayment_request() {
        return payment_request;
    }

    public void setPayment_request(PaymentRequestPacket payment_request) {
        this.payment_request = payment_request;
    }
    
}

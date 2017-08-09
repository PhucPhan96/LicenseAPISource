package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;
import nfc.messages.base.PaymentRequestPacket;

import nfc.model.Order;
import nfc.model.OrderDetail;

public class OrderView {
    
    private Order order = new Order();
    private List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
    private String customer_name;
    private PaymentRequestPacket payment_request;

    public String getCustomer_name() {
        return customer_name;
    }
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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

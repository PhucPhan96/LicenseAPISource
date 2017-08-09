/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration.endpoint;

import nfc.model.Order;
import nfc.model.PaymentOrderHistory;
import nfc.model.ViewModel.OrderView;
import nfc.service.IOrderService;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.payment.PaymentFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class PaymentServiceEndpoint {
    @Autowired
    private IOrderService orderDAO;
    
    public OrderView payment(OrderView orderView)
    {
        JSONObject resultPayment = PaymentFactory.getPaymentApi(SpeedPayInformation.PaymentAPI.SPEED_PAY).payment(orderView.getPayment_request());
        if(resultPayment.containsKey("success") && resultPayment.get("success") == "true"){
            Order order = orderView.getOrder();
            savePaymentOrderHistory(order.getOrder_id(), resultPayment);
            updatePaymentStatus(order.getOrder_id(), "PAID");
            order.setOrder_status("PAID");
        }   
        return orderView;
    }
    
    private void savePaymentOrderHistory(String orderId, JSONObject resultPayment){
        PaymentOrderHistory paymentOrderHistory = new PaymentOrderHistory();
        paymentOrderHistory.setOrder_id(orderId);
        paymentOrderHistory.setPayment_unique_number(resultPayment.get("id").toString());
        JSONObject payDetail = (JSONObject)resultPayment.get("pay_det");
        paymentOrderHistory.setCard_id(payDetail.get("card_id").toString());
        paymentOrderHistory.setCard_nm(payDetail.get("card_nm").toString());
        orderDAO.savePaymentOrderHistory(paymentOrderHistory);
    }
    
    private void updatePaymentStatus(String orderId, String status){
        orderDAO.updateOrderStatus(orderId, status);
    }
    
}

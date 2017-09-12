/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.integration.endpoint;

import nfc.messages.OrderMessage;
import nfc.model.Order;
import nfc.model.PaymentOrderHistory;
import nfc.model.ViewModel.OrderView;
import nfc.service.IOrderService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.payment.PaymentFactory;
import nfc.socket.DataQueue;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Admin
 */
public class PaymentServiceEndpoint {
    @Autowired
    private IOrderService orderDAO;
    @Autowired
    private IUserService userDAO;
    
    public OrderView payment(OrderView orderView)
    {
        JSONObject resultPayment = PaymentFactory.getPaymentApi(orderView.getPayment_request().getPayment_code()).payment(orderView.getPayment_request());
        System.err.println(resultPayment.toJSONString());
        Order order = orderView.getOrder();
        if(resultPayment.containsKey("success") && resultPayment.get("success") == "true"){
            savePaymentOrderHistory(order.getOrder_id(), resultPayment, orderView.getPayment_request().getPayment_code());
            updatePaymentStatus(order.getOrder_id(), Utils.ORDER_PAID);
            order.setOrder_status(Utils.ORDER_PAID);
            sendOrderToStore(orderView);
        }   
        else{
            order.setOrder_status(Utils.ORDER_FAILED);
            orderDAO.updateOrderStatus(order.getOrder_id(), Utils.ORDER_FAILED);
            sendOrderToStore(orderView);
        }
        return orderView;
    }
    
    private void savePaymentOrderHistory(String orderId, JSONObject resultPayment, String payment_code){
        PaymentOrderHistory paymentOrderHistory = new PaymentOrderHistory();
        paymentOrderHistory.setOrder_id(orderId);
        paymentOrderHistory.setPayment_unique_number(resultPayment.get("id").toString());
        JSONObject payDetail = (JSONObject)resultPayment.get("pay_det");
        paymentOrderHistory.setCard_id(payDetail.get("card_id").toString());
        paymentOrderHistory.setCard_nm(payDetail.get("card_nm").toString());
        paymentOrderHistory.setPayment_code(payment_code);
        orderDAO.savePaymentOrderHistory(paymentOrderHistory);
    }
    
    private void updatePaymentStatus(String orderId, String status){
        orderDAO.updateOrderStatus(orderId, status);
    }
    
    private void sendOrderToStore(OrderView orderView){
        OrderMessage orderMessage = new OrderMessage(userDAO.getUserIdOfSupplier(orderView.getOrder().getSuppl_id()));
        //orderMessage.setCustomer_name(orderView.getCustomer_name());
        orderMessage.setLstOrderDetail(orderView.getLstOrderDetail());
        orderMessage.setOrder(orderView.getOrder());
        DataQueue.getInstance().addDataQueue(orderMessage);
    }
}

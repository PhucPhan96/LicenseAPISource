package nfc.serviceImpl.integration.endpoint;

import nfc.messages.OrderStatusCustomerMessage;
import nfc.messages.OrderStatusMessage;
import nfc.messages.OrderStatusRequest;
import nfc.messages.SpeedPayCancel;
import nfc.messages.base.BasePacket;
import nfc.model.PaymentOrderHistory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.ViewModel.OrderView;
import nfc.service.IOrderService;
import nfc.service.IUserService;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.payment.PaymentFactory;
import nfc.socket.CustomerDataQueue;
import nfc.socket.DataQueue;
import org.json.simple.JSONObject;

public class OrderServiceEndpoint {
	@Autowired
	private IOrderService orderDAO;
        @Autowired
        private IUserService userDAO;
	public OrderView saveOrder(OrderView orderView)
	{
            if(!orderDAO.insertOrderView(orderView)){
                orderView.getOrder().setOrder_status(Utils.ORDER_FAILED);
            }
            return orderView;
	}
	public OrderView updateOrder(OrderView orderView){
            //orderDAO.updateOrderView(orderView);
            return orderView;
	}
        
        public OrderStatusRequest updateStatusOrder(OrderStatusRequest orderStatusRequest){
            if(!orderStatusRequest.getStatus().equals(Utils.ORDER_CANCEL)){
                if(!orderDAO.updateOrderStatus(orderStatusRequest.getOrderId(), orderStatusRequest.getStatus())){
                    orderStatusRequest.setStatus(Utils.ORDER_FAILED);
                }
            }
            return orderStatusRequest;
        }
        
        public OrderStatusRequest orderComplete(OrderStatusRequest orderStatusRequest){
            //send data to delivery system
            sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.COMPLETE_ORDER);
            sendDataToCustomer(orderStatusRequest, BasePacket.PacketType.COMPLETE_ORDER);
            return orderStatusRequest;
        }
        
        public OrderStatusRequest orderCancel(OrderStatusRequest orderStatusRequest){
            if(cancelOrder(orderStatusRequest.getOrderId())){
                orderDAO.updateOrderStatus(orderStatusRequest.getOrderId(), Utils.ORDER_CANCEL);
                sendDataToCustomer(orderStatusRequest, BasePacket.PacketType.CANCEL_ORDER);
                sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.CANCEL_ORDER);
            }
            else{
                orderStatusRequest.setStatus(Utils.ORDER_FAILED);
            }
            return orderStatusRequest;
            
        }
        
        public OrderStatusRequest orderRequestCancel(OrderStatusRequest orderStatusRequest){
            sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.REQUEST_CANCEL_ORDER);
            return orderStatusRequest;
        }
        
        public OrderStatusRequest orderCooking(OrderStatusRequest orderStatusRequest){
            sendDataToCustomer(orderStatusRequest, BasePacket.PacketType.COOKING_ORDER);
            sendOrderStatusToStore(orderStatusRequest, BasePacket.PacketType.COOKING_ORDER);
            return orderStatusRequest;
        }
        
        
        private void sendOrderStatusToStore(OrderStatusRequest orderStatusRequest, BasePacket.PacketType type){
            OrderStatusMessage orderStatusMessage = new OrderStatusMessage(orderStatusRequest.getUserIdStore(), type);
            orderStatusMessage.setStoreId(orderStatusRequest.getStoreId());
            orderStatusMessage.setOrderId(orderStatusRequest.getOrderId());
            orderStatusMessage.setStatus(orderStatusRequest.getStatus());
            DataQueue.getInstance().addDataQueue(orderStatusMessage);
        }
        
        private void sendDataToCustomer(OrderStatusRequest orderStatusRequest, BasePacket.PacketType type){
            OrderStatusCustomerMessage orderStatusCustomerMessage = new OrderStatusCustomerMessage(orderStatusRequest.getUuid(), type);
            orderStatusCustomerMessage.setOrderId(orderStatusRequest.getOrderId());
            CustomerDataQueue.getInstance().addDataQueue(orderStatusCustomerMessage);
        }
        
        private boolean cancelOrder(String orderId){
            try{
                PaymentOrderHistory paymentHistory = orderDAO.getPaymentOrderHistory(orderId);
                SpeedPayCancel speedPayCancel = new SpeedPayCancel();
                speedPayCancel.setId(paymentHistory.getPayment_unique_number());
                JSONObject resultPayment = PaymentFactory.getPaymentApi(SpeedPayInformation.PaymentAPI.SPEED_PAY).cancel(speedPayCancel);
                if(resultPayment.containsKey("success") && resultPayment.get("success") == "true"){
                    return true;
                }
                return false;
            }
            catch(Exception ex){
                return false;
            } 
        }
        
}

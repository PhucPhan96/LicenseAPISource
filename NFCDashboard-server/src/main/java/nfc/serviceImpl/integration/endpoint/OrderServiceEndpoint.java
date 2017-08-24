package nfc.serviceImpl.integration.endpoint;

import java.math.BigDecimal;
import java.util.Date;
import nfc.messages.OrderStatusCustomerMessage;
import nfc.messages.OrderStatusMessage;
import nfc.messages.OrderStatusRequest;
import nfc.messages.SpeedPayCancel;
import nfc.messages.base.BasePacket;
import nfc.model.Discount;
import nfc.model.Order;
import nfc.model.PaymentOrderHistory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.ViewModel.OrderView;
import nfc.service.IDiscountService;
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
        @Autowired
        private IDiscountService discountDAO;
        
	public OrderView saveOrder(OrderView orderView)
	{
            
            setupOderView(orderView);
            if(!orderDAO.insertOrderView(orderView)){
                System.err.println("Set Faile");
                orderView.getOrder().setOrder_status(Utils.ORDER_FAILED);
            }
            return orderView;
	}
        
        private void setupOderView(OrderView orderView){
            System.err.println("vao setup order view");
            Order order  = orderView.getOrder();
            setDiscountOrder(order);
            setOnlineFeeOrder(order);
            setTaxFee(order);
            setCouponFee(order);
            setDeliveryFee(order);
            calculatorOrderAmount(order);
            setDateForOrder(order);
            System.err.println("vao setup order view xong");
        }
        
        private void setDiscountOrder(Order order){
            Discount discount = discountDAO.getDiscountOfStore(order.getSuppl_id());
            System.err.println(discount);
            BigDecimal valueDiscount = BigDecimal.ZERO;
            if(discount != null){
                if(discount.getDiscount_value().contains("%")){
                    valueDiscount = order.getProd_amt().multiply(BigDecimal.valueOf(Integer.parseInt(discount.getDiscount_value().substring(0, discount.getDiscount_value().lastIndexOf("%")))))
                                                        .divide(BigDecimal.valueOf(100));
                }
                else {
                    valueDiscount = order.getProd_amt().subtract(BigDecimal.valueOf(Integer.parseInt(discount.getDiscount_value())));
                }
                
            }
            order.setDisc_amt(valueDiscount);
        }
        
        private void setOnlineFeeOrder(Order order){
            order.setOnline_fee(order.getProd_amt().multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(100)));
        }
        
        private void setTaxFee(Order order){
            order.setTax_amt(order.getOnline_fee().multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100)));
        }
        
        private void setCouponFee(Order order){
            order.setDiscount_coupon(BigDecimal.ZERO);
        }
        
        private void setDeliveryFee(Order order){
            order.setDeliver_amt(BigDecimal.ZERO);
        }
        
        private void calculatorOrderAmount(Order order){
            order.setOrder_amt(order.getProd_amt().subtract(order.getOnline_fee())
                                                  .subtract(order.getTax_amt())
                                                  .subtract(order.getDeliver_amt())
                                                  .subtract(order.getDisc_amt())
                                                  .add(order.getDiscount_coupon()));
        }
        
        private void setDateForOrder(Order order){
            order.setOrder_date(new Date());
            order.setDeliver_date(new Date());
            order.setRequired_date(new Date());
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

package nfc.serviceImpl.integration.endpoint;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import nfc.model.ViewModel.OrderView;

public class OrderRouter {
	public String routeOrder(OrderView orderView) {
            String result = "orderFailed"; 
            if(orderView.getOrder().getOrder_status().trim().equals("Failed")){
                result = "outputChannel";
            }
            else{
                result = "paymentChannel";
            }
            return result;	
	}
}

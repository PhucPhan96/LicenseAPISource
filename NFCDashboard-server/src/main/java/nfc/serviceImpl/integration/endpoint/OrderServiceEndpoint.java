package nfc.serviceImpl.integration.endpoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.splitter.DefaultMessageSplitter;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.MessageHandler;

import nfc.model.Order;
import nfc.model.ViewModel.OrderView;
import nfc.service.IGroupService;
import nfc.service.IOrderService;

import com.mysql.jdbc.Statement;

public class OrderServiceEndpoint {
	@Autowired
	private IOrderService orderDAO;
	public OrderView saveOrder(OrderView orderView)
	{
		orderDAO.insertOrderView(orderView);
		
		
		return orderView;
	}
	public OrderView updateOrder(OrderView orderView){
		orderDAO.updateOrderView(orderView);
		return orderView;
	}
	public MessageHandler splitter() {
		DefaultMessageSplitter splitter = new DefaultMessageSplitter();
		splitter.setOutputChannelName("headerEnricherChannel");
		return splitter;
	}
}

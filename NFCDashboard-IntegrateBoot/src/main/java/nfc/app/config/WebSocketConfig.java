package nfc.app.config;

import java.util.ArrayList;
import java.util.List;

import nfc.app.common.Utils;
import nfc.app.order.Order;
import nfc.app.order.OrderDetail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/*
@Configuration
@EnableAutoConfiguration*/
/*@ImportResource("classpath:/config/order-context.xml")*/
public class WebSocketConfig {
	/*@Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/time").setAllowedOrigins("*").withSockJs();
	}*/
	/*@Bean
	public MessageChannel requestChannel() {
		return new DirectChannel();
	}
	@Transformer(inputChannel="requestChannel", outputChannel="orders")
	public Order jsonToOrder(String data)
	{
		
		System.out.println("Vao request Channel roi ne");
		//Order order = new Order();
		JSONObject orderJsonObj = Utils.convertStringToJsonObject(data);
		Order order = (Order) Utils.convertJsonObjectToClass(orderJsonObj, "nfc.app.order.Order", new String[]{"orderDetails"});
		if(orderJsonObj.containsKey("orderDetails"))
		{
			List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
			JSONArray arrOrderDetail = (JSONArray) orderJsonObj.get("orderDetails");
			for(int i=0;i<arrOrderDetail.size();i++)
			{
				JSONObject jsonOrderDetail = (JSONObject) arrOrderDetail.get(i);
				OrderDetail orderDetail = (OrderDetail) Utils.convertJsonObjectToClass(jsonOrderDetail, "nfc.app.order.OrderDetail", new String[]{});
				lstOrderDetail.add(orderDetail);
			}
			order.setOrderDetails(arrOrderDetail);	
		}
		return order;
	}*/
	
}

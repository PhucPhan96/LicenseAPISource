package nfc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.integration.RequestGateway;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@ComponentScan
@Configuration
@RestController
public class OrderManagementController {
	AbstractApplicationContext context = null;
	RequestGateway requestGateway;
	public OrderManagementController(){
		context = new ClassPathXmlApplicationContext("/config/integration-context.xml", OrderManagementController.class);
        requestGateway = context.getBean("requestGateway", RequestGateway.class);
	}
	
	
	
	@Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/order").setAllowedOrigins("*").withSockJs();
		//new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
	}
    @Bean
    MessageHandler webSocketOutboundAdapter() {
        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
    }
	@Bean(name = "webSocketFlow.input")
    MessageChannel sendToStore() {
        return new DirectChannel();
    }
	
	/*@Bean
	MessageChannel requestChannel(){
		return new DirectChannel();
	}
	@Bean
	MessageChannel orders(){
		return new DirectChannel();
	}
	@Transformer(inputChannel="requestChannel", outputChannel="orders")
	public Order tranJsonToOrder(String data)
	{
		System.out.println("Data ne " + data);
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
			//order.setOrderDetails(lstOrderDetail);	
		}
		return order;
	}
	*/
	
   /* @Bean
    IntegrationFlow webSocketFlow() {
        return f -> {
            Function<Message , Object> splitter = m -> serverWebSocketContainer()
                    .getSessions()
                    .keySet()
                    .stream()
                    .map(s -> MessageBuilder.fromMessage(m)
                            .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
                            .build())
                    .collect(Collectors.toList());
            f.split( Message.class, splitter)
                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
                    .handle(webSocketOutboundAdapter());
        };
    }*/
    @RequestMapping("/order/{name}")
    public void send(@PathVariable String name) {
    	requestGateway.echo("aaaaa");
    	//requestGateway.echo(name);
	    
        //requestChannel().send(MessageBuilder.withPayload(name).build());
    }
    @RequestMapping( value = "/receiveGateway/{data}", method = RequestMethod.GET )
    public String testGateway(@PathVariable String data )
    {
    	System.out.println("receive roi ne");
    	sendToStore().send(MessageBuilder.withPayload(data + "aaaa").build());
    	return "AAAA";
    }
}

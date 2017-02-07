package nfc.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import nfc.model.Group;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.PosDetailView;
import nfc.model.ViewModel.SupplierView;
import nfc.service.IOrderService;
import nfc.service.IPosService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;
import nfc.serviceImpl.integration.RequestGateway;

import org.hibernate.annotations.common.reflection.java.generics.TypeEnvironmentFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.splitter.DefaultMessageSplitter;
import org.springframework.integration.websocket.IntegrationWebSocketContainer;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Configuration
@ComponentScan
@EnableIntegration
public class OrderManagementController {
	@Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
	RequestGateway requestGateway;
    @Autowired
    IOrderService orderDAO;
    @Autowired
    IPosService posDAO;
    @Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/notify").setAllowedOrigins("*").withSockJs();
		//return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
	}
    @Bean(name = "webSocketFlow.input")
    MessageChannel sendToStore() {
        return new DirectChannel();
    }
    
    @Bean
   	public ServerWebSocketContainer serverWebSocketContainerCustomer() {
   		return new ServerWebSocketContainer("/customer").setAllowedOrigins("*").withSockJs();
   		//return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
   	}
    @Bean(name = "webSocketFlowCustomer.input")
    MessageChannel sendToCustomer() {
        return new DirectChannel();
    }
    MessageChannel prepareSendToStore;
    @Bean
    MessageHandler webSocketOutboundAdapter() {
        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
    }
    
    @Bean
    MessageHandler webSocketOutboundAdapterCustomer() {
        return new WebSocketOutboundMessageHandler(serverWebSocketContainerCustomer());
    }
//    @Bean
//    IntegrationFlow webSocketFlow() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                    .map(s -> MessageBuilder.fromMessage(m)
//                            .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
//                            .build())
//                    .collect(Collectors.toList());
//            f.split( Message.class, splitter)
//                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
//                    .handle(webSocketOutboundAdapter());
//        };
//    }
//    @Bean
//    IntegrationFlow webSocketFlowCustomer() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainerCustomer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                   .map(s -> MessageBuilder.fromMessage(m)
//                           .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
//                            .build())
//                    .collect(Collectors.toList());
//            f.split( Message.class, splitter)
//                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
//                    .handle(webSocketOutboundAdapterCustomer());
//        };
//    }
    @RequestMapping(value="/order/customer", method = RequestMethod.POST)
    public void send(@RequestBody OrderView orderView) {
    	try{                                                                                                                
    		/*String token = request.getHeader(tokenHeader);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            java.util.Date date = new java.util.Date();
    		Date dateSql = new Date(date.getYear(), date.getMonth(), date.getDate());
            OrderView orderView = new OrderView();
            orderView.setCustomer_name("Kekekeke");
            orderView.getOrder().setUser_id("e56f5a26-2272-410d-9713-4e4a54093d88");
            orderView.getOrder().setOrder_status("new");
            orderView.getOrder().setSuppl_id(18);
            orderView.getOrder().setApp_id(Utils.appId);
            orderView.getOrder().setDeliver_amt(BigDecimal.valueOf(0));
            orderView.getOrder().setDeliver_date(dateSql);
            orderView.getOrder().setDisc_amt(BigDecimal.valueOf(0));
            orderView.getOrder().setDisc_rate(BigDecimal.valueOf(0));
            orderView.getOrder().setOrder_amt(BigDecimal.valueOf(0));
            orderView.getOrder().setOrder_date(dateSql);
            orderView.getOrder().setProd_amt(BigDecimal.valueOf(0));
            orderView.getOrder().setRequired_date(dateSql);
            orderView.getOrder().setTax_amt(BigDecimal.valueOf(0));
            System.out.println("chuan bi vao request");*/
            //System.out.println(requestGateway);
            
        	requestGateway.echo(orderView);
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	
    	//requestGateway.echo(name);
	    //return null;
        //requestChannel().send(MessageBuilder.withPayload(name).build());
    }
    @RequestMapping( value = "/receiveGateway", method = RequestMethod.POST )
    public @ResponseBody void testGateway(@RequestBody String data )
    {
    	try{
    		System.out.println("data" + data);
    		sendToStore().send(MessageBuilder.withPayload(data).build());
    		JSONObject jsonObject = Utils.convertStringToJsonObject(data);
    		JSONObject order = (JSONObject) jsonObject.get("order");
    		sendToCustomer().send(MessageBuilder.withPayload(order.get("user_id")).build());
    	}
    	catch(Exception ex){
    		System.out.println("vao nay ne");
    	}
    }
    @RequestMapping(value="order/pos",method=RequestMethod.GET)
	public List<OrderView> getListOrderPosView(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<OrderView>  lstOrderView = orderDAO.getListOrderViewForPos(username);
        return lstOrderView;
		//return Utils.convertObjectToJsonString(lstOrderView);
	}
    @RequestMapping(value="pos/detail/{id}",method=RequestMethod.GET)
	public PosDetailView getListOrderPosDetailView(@PathVariable("id") int orderId ,HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        PosDetailView  posDetailView = posDAO.getPosDetailView(orderId);
        return posDetailView;
		//return Utils.convertObjectToJsonString(posDetailView);
	}
    @RequestMapping(value="order/search",method=RequestMethod.POST)
	public @ResponseBody List<OrderView> getListOrderSearch(@RequestBody Map<String,String> data){
    	List<OrderView>  lstOrderView = orderDAO.getListOrderViewSearch(data.get("dateFrom"), data.get("dateTo"));
    	return lstOrderView;
		//return Utils.convertObjectToJsonString(lstOrderView);
	}
    @RequestMapping(value="order/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteRole(@PathVariable("id") int orderId){
		String data = orderDAO.deleteOrderView(orderId) + "";
		return "{\"result\":\"" + data + "\"}";
	}
    @RequestMapping(value="app/orderCount/{id}", method=RequestMethod.GET)
	public @ResponseBody String getOrderCount(@PathVariable("id") int supplierId){
		String data = orderDAO.getOrderCount(supplierId);
		return "{\"result\":\"" + data + "\"}";
	}
}

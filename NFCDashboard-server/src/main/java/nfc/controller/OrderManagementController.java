package nfc.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import nfc.model.Code;
import nfc.model.Group;
import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.ViewModel.OrderView;
import nfc.model.ViewModel.PosDetailView;
import nfc.model.ViewModel.SupplierView;
import nfc.service.ICodeService;
import nfc.service.IOrderService;
import nfc.service.IPosService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import nfc.serviceImpl.integration.OrderGateway;


@RestController
//@Configuration
//@ComponentScan
//@EnableIntegration
public class OrderManagementController {
    
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    OrderGateway orderGateway;
    @Autowired
    IOrderService orderDAO;
    @Autowired
    IPosService posDAO;
    @Autowired
    private ICodeService codeDAO;
//    @Autowired 
//    private SocketIntegrationService integrationService;    
//    public OrderManagementController(){
//        new DataQueue();
//    }
    
//    @Bean
//    public ServerWebSocketContainer serverWebSocketContainer() {
//            return new ServerWebSocketContainer("/notify").setAllowedOrigins("*").withSockJs();
//            //return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
//    }
//    
//    @Bean(name = "webSocketFlow.input")
//    MessageChannel sendToStore() {
//        return new DirectChannel();
//    }
//    
//    @Bean
//    public ServerWebSocketContainer serverWebSocketContainerCustomer() {
//            return new ServerWebSocketContainer("/customer").setAllowedOrigins("*").withSockJs();
//            //return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
//    }
//        
//    @Bean(name = "webSocketFlowCustomer.input")
//    MessageChannel sendToCustomer() {
//        return new DirectChannel();
//    }
//    
//    MessageChannel prepareSendToStore;
//    @Bean
//    MessageHandler webSocketOutboundAdapter() {
//        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
//    }
//    
//    @Bean
//    MessageHandler webSocketOutboundAdapterCustomer() {
//        return new WebSocketOutboundMessageHandler(serverWebSocketContainerCustomer());
//    }
//    
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
//    
//    @Bean
//    IntegrationFlow webSocketFlowCustomer() {
//        return f -> {
//            Function<Message , Object> splitter = m -> serverWebSocketContainerCustomer()
//                    .getSessions()
//                    .keySet()
//                    .stream()
//                   .map(s -> MessageBuilder.fromMessage(m)
//                             .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
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
            orderGateway.sendOrder(orderView);
            //return "{\"result\":\"true\"}";
    	}
    	catch(Exception ex)
    	{
            System.out.println("Error " + ex.getMessage());
            //return "{\"result\":\"false\"}";
    	}
    }
    
    @RequestMapping(value="order/pos",method=RequestMethod.GET)
	public List<OrderView> getListOrderPosView(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<OrderView>  lstOrderView = orderDAO.getListOrderViewForPos(username);
        return lstOrderView;
    }
        
    @RequestMapping(value="pos/detail/{id}",method=RequestMethod.GET)
	public PosDetailView getListOrderPosDetailView(@PathVariable("id") String orderId ,HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        PosDetailView  posDetailView = posDAO.getPosDetailView(orderId);
        return posDetailView;
    }
        
    @RequestMapping(value="order/search",method=RequestMethod.POST)
	public @ResponseBody List<OrderView> getListOrderSearch(@RequestBody Map<String,String> data){
    	List<OrderView>  lstOrderView = orderDAO.getListOrderViewSearch(data.get("dateFrom"), data.get("dateTo"));
    	return lstOrderView;
    }
        
    @RequestMapping(value="order/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deleteRole(@PathVariable("id") String orderId){
        String data = orderDAO.deleteOrderView(orderId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="app/orderCount/{id}", method=RequestMethod.GET)
    public @ResponseBody String getOrderCount(@PathVariable("id") int supplierId){
        String data = orderDAO.getOrderCount(supplierId);
        return "{\"result\":\"" + data + "\"}";
    }
    
    
    @RequestMapping(value="/order/customer/test", method = RequestMethod.GET)
    public String sendTest() {
    	OrderView orderView = new OrderView();
        Order order = new Order();
        order.setSuppl_id(1111);
        orderView.setOrder(order);
        orderGateway.sendOrder(orderView);
        return orderGateway.receive().getOrder().getSuppl_id() + "";
    }
    
}

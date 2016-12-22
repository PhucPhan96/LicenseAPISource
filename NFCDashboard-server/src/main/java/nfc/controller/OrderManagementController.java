package nfc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.ViewModel.OrderView;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.integration.RequestGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
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
import org.springframework.web.bind.annotation.RestController;
@RestController
@Configuration
@ComponentScan
public class OrderManagementController {
	@Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
	RequestGateway requestGateway;
    ServerWebSocketContainer sever;
	/*@Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/notify").setAllowedOrigins("*").withSockJs();
		//return new ServerWebSocketContainer.SockJsServiceOptions().setHeartbeatTime(60_000)
	}
    @Bean
    MessageHandler webSocketOutboundAdapter() {
    	System.out.println("Vao websocket ouput");
        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
    }
	@Bean(name = "webSocketFlow.input")
    MessageChannel sendToStore() {
        return new DirectChannel();
    }
    @Bean
    IntegrationFlow webSocketFlow() {
    	System.out.println("Vao integration Flow");
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
    }
    @Bean
	@ServiceActivator(inputChannel = "webSocketFlow.input")
	public MessageHandler splitter() {
		DefaultMessageSplitter splitter = new DefaultMessageSplitter();
		splitter.setOutputChannelName("headerEnricherChannel");
		return splitter;
	}*/
    @RequestMapping(value="/order/customer", method = RequestMethod.GET)
    public String send(HttpServletRequest request) {//@RequestBody OrderView orderView, 
    	String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        OrderView orderView = new OrderView();
        orderView.getOrder().setOrder_status("newOrder");
        orderView.setUsername(username);
        System.out.println("chuan bi vao request");
        //System.out.println(requestGateway);
    	requestGateway.echo(orderView);
    	//requestGateway.echo(name);
	    return "aaaa";
        //requestChannel().send(MessageBuilder.withPayload(name).build());
    }
    @RequestMapping( value = "/receiveGateway/{data}", method = RequestMethod.GET )
    public String testGateway(@PathVariable String data )
    {
    	System.out.println("receive roi ne");
    	//template.convertAndSend("/topic/order", "Other");
    	//sendToStore().send(MessageBuilder.withPayload(data + "aaaa").build());
    	return "AAAA";
    }
}

package nfc.app;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import nfc.app.order.Order;
import nfc.app.order.RequestGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.splitter.DefaultMessageSplitter;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.support.ExpressionEvaluatingHeaderValueMessageProcessor;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
@ImportResource("classpath:/config/order-context.xml")
public class Application {
	@Autowired
	RequestGateway requestGateway;
	public static void main(String[] args) throws Exception {
		/*AbstractApplicationContext context = null;
	    if(args.length > 0) {
	        context = new FileSystemXmlApplicationContext(args);
	    }
	    else {
	        context = new ClassPathXmlApplicationContext("/config/order-context.xml", Application.class);
	        requestGateway = context.getBean("requestGateway", RequestGateway.class);
	    }
	    context.start();*/
	   /* Order order = (Order) context.getBean("order");
	    OrderDetail orderDetail = new OrderDetail();
	    OrderItem orderItem1 = new OrderItem();
	    orderItem1.setDelivery(true);
	    OrderItem orderItem2 = new OrderItem();
	    orderItem2.setDelivery(false);
	    orderDetail.addItem(orderItem1);
	    orderDetail.addItem(orderItem2);
	    for (int i = 0; i < 2; i++) {
	    	order.process(orderDetail);
	    }*/
		
        SpringApplication.run(Application.class, args);
    }
	@Bean
	public ServerWebSocketContainer serverWebSocketContainer() {
		return new ServerWebSocketContainer("/time").setAllowedOrigins("*").withSockJs();
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

    @Bean
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
    }

    @RequestMapping("/order/{name}")
    public void send(@PathVariable String name) {
    	requestGateway.echo(name);
	    
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

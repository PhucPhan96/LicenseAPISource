package nfc.serviceImpl.integration.endpoint;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

public class WebSocketSessionsMessageSource {
	@Autowired
	ServerWebSocketContainer serverWebSocketContainer;
	public MessageSource<?> webSocketSessionsMessageSource() {
		return new MessageSource<Iterator<String>>() {

			@Override
			public Message<Iterator<String>> receive() {
				return new GenericMessage<Iterator<String>>(serverWebSocketContainer.getSessions().keySet().iterator());
			}

		};
	}
}

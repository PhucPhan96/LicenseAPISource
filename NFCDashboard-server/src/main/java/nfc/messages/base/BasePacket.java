/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages.base;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class BasePacket implements Serializable, Cloneable {
    
	protected PacketType pkt_type;

	public void setPktType(PacketType type) {
		this.pkt_type = type;
	}

	public PacketType getType() {
		return this.pkt_type;
	}

	@Override
	public BasePacket clone() throws CloneNotSupportedException {
		BasePacket data = (BasePacket) super.clone();
		return data;
	}

	public enum PacketType {
		REGISTER, /**
		 * socket ?���? packet
		 */
		REGISTER_ACK, /**
		 * REGISTER?�? ???�� ACK
		 */
		ORDER_DATA, /**
		 * 주문 ??�??�?��
		 */
		ORDER_DATA_FROM_REST, /**
		 * 주문??�??�?�� From REST WAS
		 */
		COMPLETE_PAYMENT, /**
		 * 결제 ?���?
		 */
		ORDER_CANCEL_DATA, /**
		 * 주문 취소 ??�??�?��
		 */
		//?��?�?
		CUSTOMER_REGISTER, /**
		 * ?��?�???� socket?���? ?���? packet ?��?��
		 */
		CUSTOMER_UNREGISTER, /**
		 * ?��?�???� socket?���? ?���?해�? packet ?��?��.
		 */
		//		ORDER_NUMBERING, /** 번호?�� */
		COMPLETE_NUMBERING, /**
		 * �?�?(조리, 배달) ?���?
		 */
		PREPAY_ORDER_DATA, /**
		 * ?��결제 주문
		 */
		STORE_REGISTER,
		
		REQUEST_CANCEL_ORDER,
                
                REPLY_CANCEL_ORDER

	}
}

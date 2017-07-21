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
		 * socket ?“±ë¡? packet
		 */
		REGISTER_ACK, /**
		 * REGISTER?—? ???•œ ACK
		 */
		ORDER_DATA, /**
		 * ì£¼ë¬¸ ??°??´?„°
		 */
		ORDER_DATA_FROM_REST, /**
		 * ì£¼ë¬¸??°??´?„° From REST WAS
		 */
		COMPLETE_PAYMENT, /**
		 * ê²°ì œ ?™„ë£?
		 */
		ORDER_CANCEL_DATA, /**
		 * ì£¼ë¬¸ ì·¨ì†Œ ??°??´?„°
		 */
		//?šŒ?›?
		CUSTOMER_REGISTER, /**
		 * ?šŒ?›???´ socket?„œë²? ?“±ë¡? packet ?†¡?‹ 
		 */
		CUSTOMER_UNREGISTER, /**
		 * ?šŒ?›???´ socket?„œë²? ?“±ë¡?í•´ì§? packet ?†¡?‹ .
		 */
		//		ORDER_NUMBERING, /** ë²ˆí˜¸?‘œ */
		COMPLETE_NUMBERING, /**
		 * ì¤?ë¹?(ì¡°ë¦¬, ë°°ë‹¬) ?™„ë£?
		 */
		PREPAY_ORDER_DATA, /**
		 * ?„ ê²°ì œ ì£¼ë¬¸
		 */
		STORE_REGISTER,
		
		REQUEST_CANCEL_ORDER,
                
                REPLY_CANCEL_ORDER

	}
}

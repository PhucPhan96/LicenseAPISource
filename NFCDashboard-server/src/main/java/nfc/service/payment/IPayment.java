/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.service.payment;

import nfc.messages.base.PaymentCancelPacket;
import nfc.messages.base.PaymentRequestPacket;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public interface IPayment {
    JSONObject payment(PaymentRequestPacket paymentRequest);
    JSONObject cancel(PaymentCancelPacket paymentCancelRequest);
}

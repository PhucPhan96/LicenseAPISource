/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import nfc.messages.request.PaymentCancel;
import nfc.messages.base.PaymentCancelPacket;
import nfc.messages.base.PaymentRequestPacket;
import nfc.messages.request.PayRequest;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public abstract class PaymentAbstract {
    protected String payment_code = "0";
    public abstract JSONObject payment(PayRequest paymentRequest);
    public abstract JSONObject cancel(PaymentCancel paymentCancelRequest);
}

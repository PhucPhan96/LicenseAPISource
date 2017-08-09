/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.logging.Logger;
import nfc.service.payment.IPayment;
import nfc.serviceImpl.common.SpeedPayInformation;
import nfc.serviceImpl.common.Utils;

/**
 *
 * @author Admin
 */
public class PaymentFactory {
    private static final Logger log =  Logger.getLogger(PaymentFactory.class.toString());
    
//    private static class SingletonHelper{
//        private static final PaymentFactory INSTANCE = new PaymentFactory(); 
//    }
//    
//    public static PaymentFactory getInstance(){
//        return SingletonHelper.INSTANCE;
//    }
    
    static IPayment paymentAPI = null;
    public static IPayment getPaymentApi(SpeedPayInformation.PaymentAPI type){
        switch(type){
            case SPEED_PAY:
                paymentAPI = SpeedPayPayment.getInstance();
                break;
            default:
                break;
        }
        return paymentAPI;
    }
    
}

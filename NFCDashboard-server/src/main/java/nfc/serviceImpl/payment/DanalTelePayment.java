/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import nfc.messages.request.PaymentCancel;

/**
 *
 * @author Admin
 */
public class DanalTelePayment extends PaymentAbstract{
    
    public DanalTelePayment(){
        this.payment_code = "DANALTELEPAY";
    }
    
    
    public boolean payment(LinkedHashMap<String, String> paymentRequest, String orderId) {
        return true;
    }

    public boolean cancel(PaymentCancel paymentCancelRequest) {
        Map TransR = new HashMap();

	TransR.put( "ID", "" );
	TransR.put( "PWD", "" );
	TransR.put( "TID", "xxxxx" );
	TransR.put( "Command", "BILL_CANCEL" );
	TransR.put( "OUTPUTOPTION", "3" );
	
	Map Res = DanalFunction.getInstance().CallTeledit(TransR);
        
	if( Res.get("Result").equals("0") ){
		return true;
	}
	else{
		return false;
	}
    }
    
}

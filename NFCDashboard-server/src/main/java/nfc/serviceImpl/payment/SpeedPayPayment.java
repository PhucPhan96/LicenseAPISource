/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.payment;

import java.util.Date;
import nfc.messages.SpeedPayCancel;
import nfc.messages.SpeedPayRequest;
import nfc.messages.base.PaymentCancelPacket;
import nfc.messages.base.PaymentRequestPacket;
import nfc.model.ViewModel.PaymentView;
import nfc.service.payment.IPayment;
import nfc.serviceImpl.common.NFCHttpClient;
import nfc.serviceImpl.common.SpeedPayInformation;
import org.apache.commons.httpclient.NameValuePair;
import org.json.simple.JSONObject;

/**
 *
 * @author Admin
 */
public class SpeedPayPayment implements IPayment{
    
    private static class SingletonHelper{
        private static final SpeedPayPayment INSTANCE = new SpeedPayPayment(); 
    }
    
    public static SpeedPayPayment getInstance(){
        return SingletonHelper.INSTANCE;
    }
        
    public JSONObject payment(PaymentRequestPacket paymentRequest) {
        SpeedPayRequest speedPayRequest = (SpeedPayRequest) paymentRequest;
        NameValuePair[] data = {
            new NameValuePair("card_no", speedPayRequest.getCard_no()),
            new NameValuePair("card_ymd", speedPayRequest.getCard_ymd()),
            new NameValuePair("card_serial", speedPayRequest.getCard_serial()),
            new NameValuePair("sign", speedPayRequest.getSign()),
            new NameValuePair("amt", speedPayRequest.getAmt() + ""),
            new NameValuePair("sell_nm", speedPayRequest.getSell_nm()),
            new NameValuePair("pg_type", "PG,VAN"),
            new NameValuePair("pay_type", "O2B2_APP"),
            new NameValuePair("product_nm", speedPayRequest.getProduct_nm()),
            new NameValuePair("buyer_nm", speedPayRequest.getBuyer_nm()),
            new NameValuePair("buyer_phone_no", speedPayRequest.getBuyer_phone_no()),
            new NameValuePair("buyer_email", speedPayRequest.getBuyer_email()),
            new NameValuePair("tax_free_yn", "true"),
            new NameValuePair("test_yn", "true"),
        };
        
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", "https://speed-pay.co.kr/api/v1/orders");
        requestHttp.put("isAuthorization", "true");
        requestHttp.put("access_token", getTokenPaymentApi());
        
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, data);
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public JSONObject cancel(PaymentCancelPacket paymentCancelRequest) {
        SpeedPayCancel speedPayRequest = (SpeedPayCancel) paymentCancelRequest;
        String url = "https://speed-pay.co.kr/api/v1/order/payments/"+ speedPayRequest.getId()+ "/cancel.json";
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", url);
        requestHttp.put("isAuthorization", "true");
        requestHttp.put("access_token", getTokenPaymentApi());
        NameValuePair[] data = {};
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, data);
        return result;
        
    }
    
    private String getTokenPaymentApi(){
        if(checkTokenExpried()){
            loginSpeedPayAPI();
        }
        return SpeedPayInformation.getInstance().getToken();
    }
    
    private boolean checkTokenExpried(){
        if(SpeedPayInformation.getInstance().getToken() != ""){
            Date currentDate = new Date();
            Date loginDate = SpeedPayInformation.getInstance().getDateLogin();
            if(currentDate.getTime() - loginDate.getTime() < SpeedPayInformation.getInstance().getExpried()){
                return false;
            }
        }
        return true;
    }
    
    private void loginSpeedPayAPI(){
        JSONObject requestHttp = new JSONObject();
        requestHttp.put("url", "https://speed-pay.co.kr/oauth/token");
        requestHttp.put("isAuthorization", "false");  
        NameValuePair[] data = {
            new NameValuePair("username", SpeedPayInformation.getInstance().getUsername()),
            new NameValuePair("password", SpeedPayInformation.getInstance().getPassword()),//gui lEN
            new NameValuePair("grant_type", "password")
        };
        JSONObject result = NFCHttpClient.getInstance().sendPost(requestHttp, data);
        setLoginInformation(result);
    }
    
    private void setLoginInformation(JSONObject result){
        if(result.containsKey("success")){
            SpeedPayInformation.getInstance().setDateLogin(new Date());
            SpeedPayInformation.getInstance().setExpried(Integer.parseInt(result.get("expired_in").toString()));
            SpeedPayInformation.getInstance().setToken(result.get("access_token").toString());
        }
        else{
            SpeedPayInformation.getInstance().setToken("");
        }
    }
    
    
}

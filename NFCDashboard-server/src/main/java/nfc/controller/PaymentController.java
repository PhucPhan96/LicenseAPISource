/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.controller;

import java.util.List;
import nfc.model.Payment;
import nfc.model.Role;
import nfc.model.ViewModel.PaymentView;
import nfc.service.IPaymentService;
import nfc.serviceImpl.common.NFCHttpClient;
import nfc.serviceImpl.common.Utils;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class PaymentController {
    
    @Autowired
    private IPaymentService paymentDAO;
    
    @RequestMapping(value="payments",method=RequestMethod.GET)
    public List<Payment> getPayments(){
        return paymentDAO.getPayments();
    }
    @RequestMapping(value="payment/view/{id}", method=RequestMethod.GET)
    public PaymentView getPaymentView(@PathVariable("id") int paymentId){
        return paymentDAO.getPaymentView(paymentId);
    }
    @RequestMapping(value="payment/view/add", method=RequestMethod.POST)
    public @ResponseBody String insertPaymentView(@RequestBody PaymentView paymentView){
        String data = paymentDAO.insertPaymentView(paymentView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="payment/view/update", method=RequestMethod.PUT)
    public @ResponseBody String editPaymentView(@RequestBody PaymentView paymentView){
        String data = paymentDAO.updatePaymentView(paymentView) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    @RequestMapping(value="payment/view/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deletePaymentView(@PathVariable("id") int paymentId){
        String data = paymentDAO.deletePaymentView(paymentId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="payment/update/default/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String updatePaymentDefault(@PathVariable("id") int paymentId){
        String data = paymentDAO.updatePaymentDefault(paymentId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
//    @RequestMapping(value="payment/login", method=RequestMethod.GET)
//    public String loginPaymentAPI(){
//        NameValuePair[] data = {
//            new NameValuePair("username", "test12"),
//            new NameValuePair("password", "1111111111"),//gui lEN
//            new NameValuePair("grant_type", "password")
//        };
//        return NFCHttpClient.getInstance().sendPost("https://speed-pay.co.kr/oauth/token", data).toJSONString();
//    }
    
}

package nfc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import nfc.service.IMailService;

@RestController
public class SMSController {
	public static final String ACCOUNT_SID = "ACf486b0ebb35102823cb3d448f77d8e3b";
	public static final String AUTH_TOKEN = "789e64c24ea5055a5819a0ca52d6bd10";
	@Autowired
	private IMailService mailDAO;
	public void sendSMS(){
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	    Message message = Message
	        .creator(new PhoneNumber("+84932362854"), new PhoneNumber("+12142544543"),
	            "sms notification from NFC")
	        .create();
		 System.out.println(message.getSid()); 
	}
	@RequestMapping(value="sms/send",method=RequestMethod.GET)
	public String getListOrderPosView(){
		sendEmail();
		return "ok";
	}
	public void sendEmail(){
		mailDAO.sendSimpleMail("kjncunn@gmail.com", "kimduy.mlcm@gmail.com", "hello", "check register from NFC");
	}
}

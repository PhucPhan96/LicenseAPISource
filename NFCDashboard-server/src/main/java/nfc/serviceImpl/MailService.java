package nfc.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import nfc.service.IMailService;

public class MailService implements IMailService{
	@Autowired
	private MailSender mailSender;
	
	public boolean sendSimpleMail(String from, String to, String subject, String msg){
		try{
			System.out.println(mailSender);
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(msg);
			mailSender.send(message);
			return true;
		}
		catch(Exception ex)
		{
			System.out.println("mail exception " + ex.getMessage());
			return false;
		}
	}
	public String sendSimpleMailStr(String from, String to, String subject, String msg){
		try{
			System.out.println(mailSender);
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(msg);
			mailSender.send(message);
			return "ok";
		}
		catch(Exception ex)
		{
			return ex.getMessage();
		}
	}
}

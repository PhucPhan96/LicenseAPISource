package nfc.service;

public interface IMailService {
	boolean sendSimpleMail(String from, String to, String subject, String msg);
	String sendSimpleMailStr(String from, String to, String subject, String msg);
}

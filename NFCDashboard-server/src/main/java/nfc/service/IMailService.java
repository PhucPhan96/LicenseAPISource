package nfc.service;

public interface IMailService {
	boolean sendSimpleMail(String from, String to, String subject, String msg);
}

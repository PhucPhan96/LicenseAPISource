package nfc.serviceImpl;

import org.hibernate.SessionFactory;

import nfc.service.IUserService;

public class UserService implements IUserService {
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}

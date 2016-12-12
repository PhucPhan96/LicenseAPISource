package nfc.serviceImpl;

import org.hibernate.SessionFactory;

import nfc.service.IOrderService;

public class OrderService implements IOrderService{
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}

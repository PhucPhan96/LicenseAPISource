package nfc.model.ViewModel;

import java.util.List;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.User;

public class PosDetailView {
	private Order order;
	private List<OrderDetailView> lstOrderDetailView;
	private User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<OrderDetailView> getLstOrderDetailView() {
		return lstOrderDetailView;
	}
	public void setLstOrderDetailView(List<OrderDetailView> lstOrderDetailView) {
		this.lstOrderDetailView = lstOrderDetailView;
	}
}

package nfc.model.ViewModel;

import java.util.List;

import nfc.model.Order;
import nfc.model.OrderDetail;

public class OrderView {
	private Order order = new Order();
	private List<OrderDetail> lstOrderDetail;
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public List<OrderDetail> getLstOrderDetail() {
		return lstOrderDetail;
	}
	public void setLstOrderDetail(List<OrderDetail> lstOrderDetail) {
		this.lstOrderDetail = lstOrderDetail;
	}
}

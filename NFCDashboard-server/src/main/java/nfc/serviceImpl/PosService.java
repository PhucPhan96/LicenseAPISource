package nfc.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nfc.model.Order;
import nfc.model.OrderDetail;
import nfc.model.User;
import nfc.model.UserAddress;
import nfc.model.ViewModel.OrderDetailView;
import nfc.model.ViewModel.PosDetailView;
import nfc.model.ViewModel.UserAddressView;
import nfc.service.IOrderService;
import nfc.service.IPosService;
import nfc.service.IProductService;
import nfc.service.ISupplierService;
import nfc.service.IUserService;

public class PosService implements IPosService{
	@Autowired
	private IOrderService orderDAO;
	@Autowired
	private IProductService productDAO;
	@Autowired
	private IUserService userDAO;
	@Autowired
	private ISupplierService supplierDAO;
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public PosDetailView getPosDetailView(int orderId) {
		PosDetailView posDetailView = new PosDetailView();
		Order order = orderDAO.getOrder(orderId);
		posDetailView.setOrder(order);
		User user = userDAO.getUser(order.getUser_id());
		posDetailView.setUser(user);
		List<OrderDetail> lstOrderDetail = orderDAO.getListOrderDetail(orderId);
		List<OrderDetailView> lstOrderDetailView = new ArrayList<OrderDetailView>();
		for(OrderDetail orderDetail: lstOrderDetail){
			System.out.println("Product ID " + orderDetail.getProd_id());
			OrderDetailView orderDetailView = new OrderDetailView();
			orderDetailView.setOrderDetail(orderDetail);
			orderDetailView.setProduct(productDAO.getProduct(orderDetail.getProd_id()));
			lstOrderDetailView.add(orderDetailView);
		}
		List<UserAddress> lstUserAddress = userDAO.getListUserAddress(user.getUser_id());
		List<UserAddressView> lstUserAddressView = new ArrayList<UserAddressView>();
		for(UserAddress userAddress: lstUserAddress){
			UserAddressView userAddressView = new UserAddressView();
			userAddressView.setIs_deliver(userAddress.isIs_deliver());
			userAddressView.setIs_main(userAddress.isIs_main());
			userAddressView.setAddressOfUser(supplierDAO.getAddress(userAddress.getAddr_id()));
			lstUserAddressView.add(userAddressView);
		}
		posDetailView.setLstOrderDetailView(lstOrderDetailView);
		posDetailView.setLstUserAddressView(lstUserAddressView);
		return posDetailView;
	}
}

package nfc.model.ViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nfc.model.OrderDetail;
import nfc.model.Product;
import nfc.model.Supplier;

public class BillHistory {
	private int order_id;
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	public Date getDeliver_date() {
		return deliver_date;
	}
	public void setDeliver_date(Date deliver_date) {
		this.deliver_date = deliver_date;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public BigDecimal getProd_amt() {
		return prod_amt;
	}
	public void setProd_amt(BigDecimal prod_amt) {
		this.prod_amt = prod_amt;
	}
	public int getProd_qty() {
		return prod_qty;
	}
	public void setProd_qty(int prod_qty) {
		this.prod_qty = prod_qty;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	private int suppl_id;
	private String user_id;
	private String app_id;
	private int prod_id;
	private Date order_date;
	private Date deliver_date;
	private String order_status;
	private BigDecimal prod_amt;
	private int prod_qty;
	private String prod_name;
	private String supplier_name;
	private BigDecimal unit_price;
//	private List<Product> lstproduct = new ArrayList<Product>();
//	private List<OrderDetail> lstOrderDetail = new ArrayList<OrderDetail>();
//	private List<Supplier> listSupplier = new ArrayList<Supplier>();
//	
	public BigDecimal getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}
	
	
	
	
	
	
	


	
}

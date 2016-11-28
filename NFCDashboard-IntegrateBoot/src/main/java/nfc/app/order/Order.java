package nfc.app.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Order {
	private String suppl_id;
	private String app_id;
	private String order_id;
	private String user_id;
	private Date required_date;
	private Date diliver_date;
	private String order_status;
	private BigDecimal prod_amt;
	private BigDecimal disc_amt;
	private BigDecimal disc_rate;
	private BigDecimal deliver_amt;
	private BigDecimal tax_amt;
	private BigDecimal order_amt;
	private String order_cmts;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getRequired_date() {
		return required_date;
	}
	public void setRequired_date(Date required_date) {
		this.required_date = required_date;
	}
	public Date getDiliver_date() {
		return diliver_date;
	}
	public void setDiliver_date(Date diliver_date) {
		this.diliver_date = diliver_date;
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
	public BigDecimal getDisc_amt() {
		return disc_amt;
	}
	public void setDisc_amt(BigDecimal disc_amt) {
		this.disc_amt = disc_amt;
	}
	public BigDecimal getDisc_rate() {
		return disc_rate;
	}
	public void setDisc_rate(BigDecimal disc_rate) {
		this.disc_rate = disc_rate;
	}
	public BigDecimal getDeliver_amt() {
		return deliver_amt;
	}
	public void setDeliver_amt(BigDecimal deliver_amt) {
		this.deliver_amt = deliver_amt;
	}
	public BigDecimal getTax_amt() {
		return tax_amt;
	}
	public void setTax_amt(BigDecimal tax_amt) {
		this.tax_amt = tax_amt;
	}
	public BigDecimal getOrder_amt() {
		return order_amt;
	}
	public void setOrder_amt(BigDecimal order_amt) {
		this.order_amt = order_amt;
	}
	public String getOrder_cmts() {
		return order_cmts;
	}
	public void setOrder_cmts(String order_cmts) {
		this.order_cmts = order_cmts;
	}
	private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
	public String getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(String suppl_id) {
		this.suppl_id = suppl_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
}

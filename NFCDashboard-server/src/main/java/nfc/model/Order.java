package nfc.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="fg_orders")
public class Order {
	private int order_id;
	private int suppl_id;
	private String user_id;
	private String app_id;
	private Date order_date;
	private Date deliver_date;
	private Date required_date;
	private String order_status;
	private BigDecimal prod_amt;
	private BigDecimal disc_amt;
	
	private BigDecimal disc_rate;
	private BigDecimal deliver_amt;
	private BigDecimal tax_amt;
	private BigDecimal order_amt;
	/*private List<OrderDetail> orderDetails;
	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}*/
	@Id
	@Column(name="order_id")
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	@Column(name="suppl_id")
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	@Column(name="user_id")
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Column(name="order_date")
	public Date getOrder_date() {
		return order_date;
	}
	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}
	@Column(name="deliver_date")
	public Date getDeliver_date() {
		return deliver_date;
	}
	public void setDeliver_date(Date deliver_date) {
		this.deliver_date = deliver_date;
	}
	@Column(name="required_date")
	public Date getRequired_date() {
		return required_date;
	}
	public void setRequired_date(Date required_date) {
		this.required_date = required_date;
	}
	@Column(name="order_status")
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	@Column(name="prod_amt")
	public BigDecimal getProd_amt() {
		return prod_amt;
	}
	public void setProd_amt(BigDecimal prod_amt) {
		this.prod_amt = prod_amt;
	}
	@Column(name="disc_amt")
	public BigDecimal getDisc_amt() {
		return disc_amt;
	}
	public void setDisc_amt(BigDecimal disc_amt) {
		this.disc_amt = disc_amt;
	}
	@Column(name="disc_rate")
	public BigDecimal getDisc_rate() {
		return disc_rate;
	}
	public void setDisc_rate(BigDecimal disc_rate) {
		this.disc_rate = disc_rate;
	}
	@Column(name="deliver_amt")
	public BigDecimal getDeliver_amt() {
		return deliver_amt;
	}
	public void setDeliver_amt(BigDecimal deliver_amt) {
		this.deliver_amt = deliver_amt;
	}
	@Column(name="tax_amt")
	public BigDecimal getTax_amt() {
		return tax_amt;
	}
	public void setTax_amt(BigDecimal tax_amt) {
		this.tax_amt = tax_amt;
	}
	@Column(name="order_amt")
	public BigDecimal getOrder_amt() {
		return order_amt;
	}
	public void setOrder_amt(BigDecimal order_amt) {
		this.order_amt = order_amt;
	}
}

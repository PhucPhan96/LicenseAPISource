package nfc.app.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail {
	private String suppl_id;
	private String app_id;
	private String order_id;
	private String prod_id;
	private String cate_id;
	private BigDecimal unit_price;
	private int prod_qty;
	private BigDecimal disc_amt;
	private int disc_rate;
	private BigDecimal deliver_amt;
	private BigDecimal prod_amt;
	private String prod_cmts;
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
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getCate_id() {
		return cate_id;
	}
	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}
	public BigDecimal getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(BigDecimal unit_price) {
		this.unit_price = unit_price;
	}
	public int getProd_qty() {
		return prod_qty;
	}
	public void setProd_qty(int prod_qty) {
		this.prod_qty = prod_qty;
	}
	public BigDecimal getDisc_amt() {
		return disc_amt;
	}
	public void setDisc_amt(BigDecimal disc_amt) {
		this.disc_amt = disc_amt;
	}
	public int getDisc_rate() {
		return disc_rate;
	}
	public void setDisc_rate(int disc_rate) {
		this.disc_rate = disc_rate;
	}
	public BigDecimal getDeliver_amt() {
		return deliver_amt;
	}
	public void setDeliver_amt(BigDecimal deliver_amt) {
		this.deliver_amt = deliver_amt;
	}
	public BigDecimal getProd_amt() {
		return prod_amt;
	}
	public void setProd_amt(BigDecimal prod_amt) {
		this.prod_amt = prod_amt;
	}
	public String getProd_cmts() {
		return prod_cmts;
	}
	public void setProd_cmts(String prod_cmts) {
		this.prod_cmts = prod_cmts;
	}
}

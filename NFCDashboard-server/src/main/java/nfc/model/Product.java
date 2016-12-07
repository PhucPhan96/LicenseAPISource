package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_products")
public class Product {
	private int prod_id;
	private String app_id;
	private String prod_name;
	private int cate_id;
	private String prod_desc;
	private int suppl_id;
	private String qty_per_unit;
	private double unit_price;
	private String prod_origins;
	private int min_order_qty;
	
	@Id
	@Column(name="prod_id")
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Column(name="prod_name")
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	@Column(name="cate_id")
	public int getCate_id() {
		return cate_id;
	}
	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}
	@Column(name="prod_desc")
	public String getProd_desc() {
		return prod_desc;
	}
	public void setProd_desc(String prod_desc) {
		this.prod_desc = prod_desc;
	}
	@Column(name="suppl_id")
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	@Column(name="qty_per_unit")
	public String getQty_per_unit() {
		return qty_per_unit;
	}
	public void setQty_per_unit(String qty_per_unit) {
		this.qty_per_unit = qty_per_unit;
	}
	@Column(name="unit_price")
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	@Column(name="prod_origins")
	public String getProd_origins() {
		return prod_origins;
	}
	public void setProd_origins(String prod_origins) {
		this.prod_origins = prod_origins;
	}
	@Column(name="min_order_qty")
	public int getMin_order_qty() {
		return min_order_qty;
	}
	public void setMin_order_qty(int min_order_qty) {
		this.min_order_qty = min_order_qty;
	}
}

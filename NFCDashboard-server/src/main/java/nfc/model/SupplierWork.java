package nfc.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_supplier_work")
public class SupplierWork {
	private int suppl_id;
	private int is_active;
	private String wd_start_hm;
	private String wd_end_hm;
	private String sat_start_hm;
	private String sat_end_hm;
	private String sun_start_hm;
	private String sun_end_hm;
	private byte is_online;
	private double rank5;
	private int order_count;
	private int favorite_count;
	private byte call_order;
	private byte direct_pay;
	private byte visit_pay;
	private double min_order_amt;
	private int owner_suppl_id;
	private int manage_suppl_id;
	
	@Id
	@Column(name="suppl_id")
	public int getSuppl_id() {
		return suppl_id;
	}
	public void setSuppl_id(int suppl_id) {
		this.suppl_id = suppl_id;
	}
	@Column(name="is_active")
	public int getIs_active() {
		return is_active;
	}
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}
	@Column(name="wd_start_hm")
	public String getWd_start_hm() {
		return wd_start_hm;
	}
	public void setWd_start_hm(String wd_start_hm) {
		this.wd_start_hm = wd_start_hm;
	}
	@Column(name="wd_end_hm")
	public String getWd_end_hm() {
		return wd_end_hm;
	}
	public void setWd_end_hm(String wd_end_hm) {
		this.wd_end_hm = wd_end_hm;
	}
	@Column(name="sat_start_hm")
	public String getSat_start_hm() {
		return sat_start_hm;
	}
	public void setSat_start_hm(String sat_start_hm) {
		this.sat_start_hm = sat_start_hm;
	}
	@Column(name="sat_end_hm")
	public String getSat_end_hm() {
		return sat_end_hm;
	}
	public void setSat_end_hm(String sat_end_hm) {
		this.sat_end_hm = sat_end_hm;
	}
	@Column(name="sun_start_hm")
	public String getSun_start_hm() {
		return sun_start_hm;
	}
	public void setSun_start_hm(String sun_start_hm) {
		this.sun_start_hm = sun_start_hm;
	}
	@Column(name="sun_end_hm")
	public String getSun_end_hm() {
		return sun_end_hm;
	}
	public void setSun_end_hm(String sun_end_hm) {
		this.sun_end_hm = sun_end_hm;
	}
	@Column(name="is_online")
	public byte getIs_online() {
		return is_online;
	}
	public void setIs_online(byte is_online) {
		this.is_online = is_online;
	}
	@Column(name="rank5")
	public double getRank5() {
		return rank5;
	}
	public void setRank5(double rank5) {
		this.rank5 = rank5;
	}
	@Column(name="order_count")
	public int getOrder_count() {
		return order_count;
	}
	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}
	@Column(name="favorite_count")
	public int getFavorite_count() {
		return favorite_count;
	}
	public void setFavorite_count(int favorite_count) {
		this.favorite_count = favorite_count;
	}
	@Column(name="call_order")
	public byte getCall_order() {
		return call_order;
	}
	public void setCall_order(byte call_order) {
		this.call_order = call_order;
	}
	@Column(name="direct_pay")
	public byte getDirect_pay() {
		return direct_pay;
	}
	public void setDirect_pay(byte direct_pay) {
		this.direct_pay = direct_pay;
	}
	@Column(name="visit_pay")
	public byte getVisit_pay() {
		return visit_pay;
	}
	public void setVisit_pay(byte visit_pay) {
		this.visit_pay = visit_pay;
	}
	@Column(name="min_order_amt")
	public double getMin_order_amt() {
		return min_order_amt;
	}
	public void setMin_order_amt(double min_order_amt) {
		this.min_order_amt = min_order_amt;
	}
	@Column(name="owner_suppl_id")
	public int getOwner_suppl_id() {
		return owner_suppl_id;
	}
	public void setOwner_suppl_id(int owner_suppl_id) {
		this.owner_suppl_id = owner_suppl_id;
	}
	@Column(name="manage_suppl_id")
	public int getManage_suppl_id() {
		return manage_suppl_id;
	}
	public void setManage_suppl_id(int manage_suppl_id) {
		this.manage_suppl_id = manage_suppl_id;
	}
}

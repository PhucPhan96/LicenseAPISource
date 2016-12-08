package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_product_categories")
public class ProductCategory {
	private int cate_id;
	private int prod_id;
	private int sort_seq;
	@Id
	@Column(name="cate_id")
	public int getCate_id() {
		return cate_id;
	}
	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}
	@Column(name="prod_id")
	public int getProd_id() {
		return prod_id;
	}
	public void setProd_id(int prod_id) {
		this.prod_id = prod_id;
	}
	@Column(name="sort_seq")
	public int getSort_seq() {
		return sort_seq;
	}
	public void setSort_seq(int sort_seq) {
		this.sort_seq = sort_seq;
	}
}

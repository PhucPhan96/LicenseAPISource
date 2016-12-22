package nfc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_supplier_users")
public class SupplierUser {
	private String app_id; 
	private int suppl_id;
	private String user_id; 
	private Boolean is_director;
	@Column(name="app_id")
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Id
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
	@Column(name="is_director", nullable=true)
	public Boolean isIs_director() {
		return is_director;
	}
	public void setIs_director(Boolean is_director) {
		this.is_director = is_director;
	}
}

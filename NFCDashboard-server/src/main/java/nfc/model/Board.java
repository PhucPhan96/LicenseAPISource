package nfc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fg_boards")
public class Board {
	private int board_id; 
	private String app_id; 
	private String owner_id; 
	private String board_name; 
	private Date created_date;
	private String owner_name;
	private List<String>  suppl_name = new ArrayList<String>();
	@Id
	@GeneratedValue
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	@javax.persistence.Transient
	public String getOwner_name() {
		return owner_name;
	}
	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}
	@javax.persistence.Transient
	public List<String> getSuppl_name() {
		return suppl_name;
	}
	public void setSuppl_name(List<String> suppl_name) {
		this.suppl_name = suppl_name;
	}
}

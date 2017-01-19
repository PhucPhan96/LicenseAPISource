package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nfc.model.Address;
import nfc.model.AttachFile;
import nfc.model.Category;
import nfc.model.Role;
import nfc.model.Supplier;
import nfc.model.SupplierAddress;
import nfc.model.SupplierWork;
import nfc.model.User;

public class SupplierView {
	private Supplier supplier = new Supplier();
	private SupplierWork supplierWork = new SupplierWork();
	private List<AttachFile> images = new ArrayList<AttachFile>();
	
	private List<Category> categories = new ArrayList<Category>();
	private List<SupplierAddressView> lstSupplAddressView = new ArrayList<SupplierAddressView>();
	private User director = new User();
	private List<SupplierAttachFileView> lstAttachFileView;
//	private String img_path;
	public User getDirector() {
		return director;
	}
	public void setDirector(User director) {
		this.director = director;
	}
	/*private List<Role> roles;*/
	//private Supplier supplierManage;
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public SupplierWork getSupplierWork() {
		return supplierWork;
	}
	public void setSupplierWork(SupplierWork supplierWork) {
		this.supplierWork = supplierWork;
	}
	public List<AttachFile> getImages() {
		return images;
	}
	public void setImages(List<AttachFile> images) {
		this.images = images;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<SupplierAddressView> getLstSupplAddressView() {
		return lstSupplAddressView;
	}
	public void setLstSupplAddressView(List<SupplierAddressView> lstSupplAddressView) {
		this.lstSupplAddressView = lstSupplAddressView;
	}

//	public String getImagePath() {
//		return img_path;
//	}
//	public void setImagePath(String img_path) {
//		this.img_path = img_path;
//	}
	/*public Supplier getSupplierManage() {
		return supplierManage;
	}
	public void setSupplierManage(Supplier supplierManage) {
		this.supplierManage = supplierManage;
	}*/
	public List<SupplierAttachFileView> getLstAttachFileView() {
		return lstAttachFileView;
	}
	public void setLstAttachFileView(List<SupplierAttachFileView> lstAttachFileView) {
		this.lstAttachFileView = lstAttachFileView;
	}
}

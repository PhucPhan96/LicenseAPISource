package nfc.model.ViewModel;

import java.util.List;

import nfc.model.AttachFile;
import nfc.model.Product;

public class ProductView {
	private Product product;
	private List<ProductAttachFileView> lstAttachFileView;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public List<ProductAttachFileView> getLstAttachFileView() {
		return lstAttachFileView;
	}
	public void setLstAttachFileView(List<ProductAttachFileView> lstAttachFileView) {
		this.lstAttachFileView = lstAttachFileView;
	}
	
}

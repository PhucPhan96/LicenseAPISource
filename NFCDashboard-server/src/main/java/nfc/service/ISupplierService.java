package nfc.service;
import java.util.List;

import nfc.model.Address;
import nfc.model.Supplier;
import nfc.model.SupplierImage;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.ViewModel.SupplierView;

public interface ISupplierService {
	List<Supplier> getListSupplier();
	List<SupplierView> getListSupplierView(String username);
	Supplier getSupplier(String supplId);
	SupplierWork getSupplierWork(int supplId);
	SupplierView getSupplierView(int supplId);
	List<SupplierImage> getListSupplierImage(int supplId);
	List<SupplierUser> getListSupplierUser(String username);
	Address getAddress(int addrId);
	boolean insertSupplierView(SupplierView supplierView, String username);
	boolean updateSupplierView(SupplierView supplierView);
	boolean deleteSupplierView(int supplId);
	
}

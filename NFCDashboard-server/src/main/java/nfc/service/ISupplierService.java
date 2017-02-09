package nfc.service;
import java.util.List;

import nfc.model.Address;
import nfc.model.Supplier;
import nfc.model.SupplierCategories;
import nfc.model.SupplierImage;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.SupplierFavorite;
import nfc.model.ViewModel.SupplierAppView;
import nfc.model.ViewModel.SupplierView;

public interface ISupplierService {
	List<Supplier> getListSupplier();
	List<SupplierView> getListSupplierView(String username);
	Supplier getSupplier(String supplId);
	Supplier getSupplierFromUser(String username);
	SupplierWork getSupplierWork(int supplId);
	SupplierView getSupplierView(int supplId);
	List<SupplierImage> getListSupplierImage(int supplId);
	List<SupplierUser> getListSupplierUser(String username);
	Address getAddress(int addrId);
	boolean insertSupplierView(SupplierView supplierView, String username);
	boolean updateSupplierView(SupplierView supplierView);
	boolean deleteSupplierView(int supplId, String username);
	List<SupplierUser> getListSupplierUserId(String userId);
	List<SupplierAppView> getListSupplierViewOfCategory(int categoryId);
	List<SupplierCategories> getListSupplierCategory(int supplId);
	String getSupplierFavorite(int supplId);
	boolean insertSupplierFavorite(int supplId, String userId);
	SupplierFavorite isSupplierFavorite(String userId);
	boolean deleteSupplierFavorite(int supplId, String userId);
	SupplierView getSupplierView2(int supplI);
	SupplierUser checkUserIsStore(String username);
}

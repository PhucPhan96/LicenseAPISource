package nfc.service;
import java.util.List;

import org.hibernate.Session;

import nfc.model.Address;
import nfc.model.Mail;
import nfc.model.Order;
import nfc.model.PKModel.SupplierUserPK;
import nfc.model.Supplier;
import nfc.model.SupplierCategories;
import nfc.model.SupplierImage;
import nfc.model.SupplierUser;
import nfc.model.SupplierWork;
import nfc.model.SupplierFavorite;
import nfc.model.ViewModel.BillHistory;
import nfc.model.ViewModel.SupplierAppView;
import nfc.model.ViewModel.SupplierView;

public interface ISupplierService {
	List<Supplier> getListSupplier();
	List<SupplierView> getListSupplierView(String username);
	Supplier getSupplier(String supplId);
	Supplier getSupplierFromUser(String username);
	SupplierWork getSupplierWork(int supplId);
	SupplierView getSupplierView(int supplId);
        SupplierView getSupplierView1(int supplId);
        boolean ChangeOrderPhoneNumberSupplier(String supplierId,String orderPhoneNum);
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
	List<Supplier> getListSupplierFavoriteByUser(String userID);
	void deleteFavoriteStore(Session session, int suppl_id);
	String deleteStoreFavorite(int supplId);
	List<BillHistory> getListBillHistory (String userID);
	List<BillHistory> getListSearchBillHistory(String userID, String dateFrom, String dateTo);
        List<Supplier> getListSupplierManage(int roleId);
        List<Supplier> getListSupplierFromRoles(String roleJoin);
        List<SupplierWork> getListSupplierWorkOfManager(int manageSupplId);
        List<SupplierView> getListSupplierViewOfManage(int supplierId);
        List<SupplierView> getListSupplierViewOfRole(int roleId);
        
        List<Supplier> getListSupplierOfManage(int supplierId);
        List<Supplier> getListSupplierOfRole(int roleId);
        List<Supplier> getListStore();
        List<SupplierView> getListSupplierByAddress(String longT, String latT);
        List<SupplierView> getListSupplierViewByTextInput(String text);
        List<Supplier> fGetListSupplierFromSuppIDManager(int supplId);
        List<Supplier> fGetListSupplierFromUserName(String userID);
        String fSendMailToSysAdmin(Mail mail);
}

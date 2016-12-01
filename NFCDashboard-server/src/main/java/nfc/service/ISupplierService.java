package nfc.service;
import java.util.List;

import nfc.model.Supplier;
import nfc.model.User;
public interface ISupplierService {
	List<Supplier> getListSupplier();
	boolean updateSupplier(Supplier supplier);
	boolean insertSupplier(Supplier supplier);
	Supplier getSupplier(String suppl_id);
}

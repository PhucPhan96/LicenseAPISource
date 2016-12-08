package nfc.service;
import java.util.List;
import nfc.model.Supplier;

public interface ISupplierService {
	List<Supplier> getListSupplier();
	Supplier getSupplier(String supplId);
}

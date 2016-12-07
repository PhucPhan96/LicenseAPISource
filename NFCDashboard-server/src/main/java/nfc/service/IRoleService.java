package nfc.service;
import java.util.List;
import nfc.model.Role;

public interface IRoleService {
	List<Role> getListRole();
	boolean insertRole(Role role);
	Role getRole(String roleId);
	boolean updateRole(Role role);
	boolean deleteRole(String roleId);
}

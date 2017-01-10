package nfc.service;

import java.util.List;

import nfc.model.User;
import nfc.model.UserAddress;
import nfc.model.UserRegister;
import nfc.model.UserRole;

public interface IUserService {
	List<User> getListUser();
	boolean updateUser(User user);
	boolean insertUser(User user);
	boolean deleteUser(String userId);
	User getUser(String userId);
	User findUserByUserName(String username);
	List<UserRole> getListUserRole(String userId);
	boolean ChangPasswordUser(String userId,String pass,String passSalt);
	List<UserAddress> getListUserAddress(String userId);
	boolean insertUserRegister(UserRegister userRegist);
	boolean updateUserRegister(UserRegister userRegist);
	UserRegister getUserRegister(String email);
	String saveUserRegister(UserRegister userRegist);
}

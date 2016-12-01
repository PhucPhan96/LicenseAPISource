package nfc.service;

import java.util.List;

import nfc.model.User;

public interface IUserService {
	List<User> getListUser();
	boolean updateUser(User user);
	boolean insertUser(User user);
	boolean deleteUser(String userId);
	User getUser(String userId);
}

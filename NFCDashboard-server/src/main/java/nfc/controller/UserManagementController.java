package nfc.controller;

import java.util.List;

import nfc.model.User;
import nfc.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserManagementController {
	@Autowired
	private IUserService userDAO;
	
	@RequestMapping("/user")
	public List<User> getUser(){
		List<User> users = userDAO.getListUser();
		System.out.println("Size " + users.size());
		return users;
	}
}

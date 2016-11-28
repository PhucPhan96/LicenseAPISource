package nfc.controller;

import nfc.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserManagementController {
	@Autowired
	private IUserService userDAO;
	@RequestMapping("/user")
	public String GetUser(){
		
		return "Welcome to RestTemplate Example.";
	}
}

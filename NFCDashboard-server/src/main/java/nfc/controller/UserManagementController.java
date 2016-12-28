package nfc.controller;

import java.util.Date;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;

import nfc.model.Category;
import nfc.model.User;
import nfc.service.IUserService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserManagementController {
	@Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private IUserService userDAO;
	
	@RequestMapping(value="user",method=RequestMethod.GET)
	public String getListUser(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
		List<User> users = userDAO.getListUser();
		User user = userDAO.findUserByUserName(username);
		return Utils.convertObjectToJsonString(users);
	}
	@RequestMapping(value="user/detail/{id}",method=RequestMethod.GET)
	public String getUser(@PathVariable("id") String userId){
		User users = userDAO.getUser(userId);	
		return Utils.convertObjectToJsonString(users);
	}
	@RequestMapping(value="user/add", method=RequestMethod.POST)
	public @ResponseBody String insertUser(@RequestBody User user){	
		user.setApp_id(Utils.appId);
		UUID uuid = UUID.randomUUID();
		String randomUUID = uuid.toString();
		user.setUser_id(randomUUID);
		Date date = new Date();
		user.setCreated_date(date);
		System.out.println("vao insert");
		System.out.println(user.getLstRoles().size());
		
		String data = userDAO.insertUser(user)+"";
		//String data = "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="user/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteUser(@PathVariable("id") String userId){
		String data = userDAO.deleteUser(userId)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="user/update", method=RequestMethod.PUT)
	public @ResponseBody String updateUser(@RequestBody User user){
		System.out.println("vao duoc update user");
		String data = userDAO.updateUser(user)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="user/changePassword",method=RequestMethod.POST)
	public @ResponseBody String ChangePasswordUser(@RequestBody String[] temp){
		System.out.println("vao change password: Pass:  " + temp[0]);
		System.out.println("vao change password: Id: " + temp[1]);
		String data = userDAO.ChangPasswordUser(temp[1],temp[0])+"";
		return "{\"result\":\"" + data + "\"}";
	}
}

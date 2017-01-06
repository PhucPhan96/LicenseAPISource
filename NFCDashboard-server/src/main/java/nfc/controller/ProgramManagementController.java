package nfc.controller;

import java.util.Date;
import java.util.List;

import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;

import nfc.model.Category;
import nfc.model.Program;
import nfc.service.IProgramService;
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
public class ProgramManagementController {
	@Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private IProgramService programDAO;
	
	@RequestMapping(value="program",method=RequestMethod.GET)
	public String getListUser(HttpServletRequest request){
		//String token = request.getHeader(tokenHeader);
        //String username = jwtTokenUtil.getUsernameFromToken(token);
		List<Program> programs = programDAO.getListProgram();
	
		return Utils.convertObjectToJsonString(programs);
	}
	@RequestMapping(value="program/add", method=RequestMethod.POST)
	public @ResponseBody String insertCategory(@RequestBody Program prog){
		prog.setApp_id(Utils.appId);
		UUID uuid = UUID.randomUUID();
		String randomUUID = uuid.toString();
		prog.setProgram_id(randomUUID);
		prog.setProgram_url("");
		System.out.println("URL la: "+ prog.getProgram_url());
		System.out.println("URL la: "+ prog.getProgram_url().length());
		String data = programDAO.insertProgram(prog)+"";
		return "{\"result\":\"" + data + "\"}";
	}
}

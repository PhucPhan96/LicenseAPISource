package nfc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.AttachFile;
import nfc.model.Board;
import nfc.service.IBoardService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;
import nfc.model.Thread;

@RestController
public class BoardManagementController {
	@Autowired
	private IBoardService boardDAO;
	@Value("Authorization")
    private String tokenHeader;
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	@RequestMapping(value="board",method=RequestMethod.GET)
	public List<Board> getBoards(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
		System.out.println("vao board:"+ username);
		List<Board> boards = boardDAO.getListBoard(username);
		return boards;
	}
	@RequestMapping(value="board/thread/{id}", method=RequestMethod.GET)
	public List<Thread> getCategory(@PathVariable("id") int board_id){
		System.out.println("boardId is: " + board_id);	
		List<Thread> threads = boardDAO.getListThread(board_id);
		System.out.println("count is: " + threads.size());
		return threads;
	} 
	@RequestMapping(value="board/thread/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteThread(@PathVariable("id") String threadId){
		System.out.println("Vao delete " + threadId);
		String data =boardDAO.deleteThread(threadId) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="board//delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteBoard(@PathVariable("id") int boardId){
		System.out.println("Vao delete " + boardId);
		String data =boardDAO.deleteBoard(boardId) + "";
		return "{\"result\":\"" + data + "\"}";
	}
}

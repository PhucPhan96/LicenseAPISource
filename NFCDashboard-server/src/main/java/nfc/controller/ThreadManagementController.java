package nfc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.ViewModel.ThreadView;
import nfc.service.IThreadService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ThreadManagementController {
    @Value("Authorization")
    private String tokenHeader;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IThreadService threadDAO;

    @RequestMapping(value="threads", method=RequestMethod.GET)
    public List<ThreadView> getListThread(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        List<ThreadView> threads = threadDAO.getListThreadView(username);
        return threads;
    }
    
    @RequestMapping(value="thread/add", method=RequestMethod.POST)
    public @ResponseBody nfc.model.Thread insertThread(@RequestBody nfc.model.Thread thread){
        return threadDAO.insertThread(thread);
    }
    
    @RequestMapping(value="thread/update", method=RequestMethod.PUT)
    public @ResponseBody String editThread(@RequestBody nfc.model.Thread thread){
        String data = threadDAO.updateThread(thread) + "";
        return "{\"result\":\"" + data + "\"}";
    }
    
    @RequestMapping(value="thread/delete/{id}", method=RequestMethod.DELETE)
    public @ResponseBody String deletePaymentView(@PathVariable("id") String threadId){
        String data = threadDAO.deleteThread(threadId) + "";
        return "{\"result\":\"" + data + "\"}";
    }
}

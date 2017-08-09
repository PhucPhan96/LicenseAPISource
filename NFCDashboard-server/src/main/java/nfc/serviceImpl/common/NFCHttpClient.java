/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.serviceImpl.common;

import java.io.InputStreamReader;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;;
/**
 *
 * @author Admin
 */
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NFCHttpClient {
    
    private static final Logger log =  Logger.getLogger(NFCHttpClient.class.toString());
    
    private static class SingletonHelper{
        private static final NFCHttpClient INSTANCE = new NFCHttpClient(); 
    }
    
    public static NFCHttpClient getInstance(){
        return SingletonHelper.INSTANCE;
    }
    
    public JSONObject  sendPost(JSONObject infoRequest, NameValuePair[] data){
        JSONObject  response= new JSONObject ();
        PostMethod post = new PostMethod(infoRequest.get("url").toString());
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        if(infoRequest.get("isAuthorization").toString() == "true" ){
            post.setRequestHeader("Authorization", "bearer " + infoRequest.get("access_token").toString());
        }
        post.setQueryString(data);
        try {
//			InputStream in = post.getResponseBodyAsStream();
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(post);

            log.info("status code: " + statusCode);
            log.info("status text: " + HttpStatus.getStatusText(statusCode));
            log.info("response body: " + post.getResponseBodyAsString());
            response = (JSONObject)(new JSONParser().parse(new InputStreamReader(post.getResponseBodyAsStream())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("error: " + e.getMessage());
        }
        return response;
    } 
    
}

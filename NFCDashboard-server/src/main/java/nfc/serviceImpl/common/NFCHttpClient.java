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
import org.apache.commons.httpclient.NameValuePair;import org.apache.commons.httpclient.methods.GetMethod;
;
/**
 *
 * @author Admin
 */
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
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
    
    public JSONObject sendPost(JSONObject infoRequest, JSONObject json){
        JSONObject  response= new JSONObject ();
        try{
            PostMethod post = new PostMethod(infoRequest.get("url").toString());
            StringRequestEntity requestEntity = new StringRequestEntity(json.toJSONString(),"application/json","UTF-8");
            if(infoRequest.get("isAuthorization").toString() == "true" ){
                post.addRequestHeader("Authorization", "bearer " + infoRequest.get("access_token").toString());
            }
            else{
                post.setRequestEntity(requestEntity);
            }
            post.setRequestEntity(requestEntity);
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
    
    public JSONObject sendGet(JSONObject infoRequest, NameValuePair[] params){
        JSONObject  response= new JSONObject ();
        try{
            GetMethod get = new GetMethod(infoRequest.get("url").toString());
            get.setQueryString(params);
            HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(get);
            log.info("status code: " + statusCode);
            log.info("status text: " + HttpStatus.getStatusText(statusCode));
            log.info("response body: " + get.getResponseBodyAsString());
            response = (JSONObject)(new JSONParser().parse(new InputStreamReader(get.getResponseBodyAsStream())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("error: " + e.getMessage());
        }
        return response;
    } 
    
}

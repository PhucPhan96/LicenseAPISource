/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.socket;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Admin
 */
public class KeyMap {
    private ConcurrentHashMap<String, CopyOnWriteArrayList<String>> map = new ConcurrentHashMap<String, CopyOnWriteArrayList<String>>();
	
	/**
	 * SelectionKey ?? 
	 * @param storename	??? ??? 
	 * @param key	SelectionKey
	 */
//	public void addKey(String storename, SelectionKey key){
//                    
//			map.put(storename, key);
//	}
        
        public void addKey(String storeId, String sessionId){
            if(map.get(storeId) == null){
                map.put(storeId, new CopyOnWriteArrayList<String>());   
            }
            map.get(storeId).add(sessionId);  
			//map.put(storename, key);
	}

	/**
	 * SelectionKey? ?? ??? 
	 */
	public void clearAll(){
		map.clear();
	}
	
	/**
	 * ???? ??? ??, ???.
	 * @param storename
	 */
	public void removeKey(String storename){
		map.remove(storename);
	}
	
        public void removeKey(String storeId, String sessionId){
            try{
                Iterator<String> it = map.get(storeId).iterator();
                while(it.hasNext()){
                    String value = it.next();
                    if(value.equals(sessionId)){
                        map.get(storeId).remove(value);
                    }
                }
                if(map.get(storeId).size() == 0){
                    map.remove(storeId);
                }
            }
            catch(Exception ex){
                System.err.println("Error " + ex.getMessage());
            }    
        }
        
        public List<String> getKey(String storename){
            return map.get(storename);
	}
	
	
//	@Override
	public String print(){
		StringBuilder sb = new StringBuilder();
		
		for(String store : map.keySet()){
			sb.append(store).append(":").append(map.get(store));
		}
		return sb.toString();
	}
}

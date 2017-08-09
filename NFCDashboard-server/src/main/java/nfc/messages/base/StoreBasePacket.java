/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages.base;

/**
 *
 * @author Admin
 */
public class StoreBasePacket  extends BasePacket {
	
	protected int store_id;
	
	public StoreBasePacket(int store_id) {
		this.store_id = store_id;
	}
	
	public int getStoreId(){
		return this.store_id;
	}
	
	
	@Override
	public StoreBasePacket clone() throws CloneNotSupportedException {
            StoreBasePacket data = (StoreBasePacket) super.clone();
            return data;
	}
	
	@Override
	public String toString(){
		return String.format("%s store_id:%s", this.getClass().getSimpleName(), store_id);
	}
}

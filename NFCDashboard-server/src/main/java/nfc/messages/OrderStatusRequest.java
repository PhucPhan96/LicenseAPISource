/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

/**
 *
 * @author Admin
 */
public class OrderStatusRequest {
    private String orderId;
    private String status;
    private int storeId;
    private String uuid;
    private String userIdStore;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserIdStore() {
        return userIdStore;
    }

    public void setUserIdStore(String userIdStore) {
        this.userIdStore = userIdStore;
    }
    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

import nfc.messages.base.PaymentCancelPacket;

/**
 *
 * @author Admin
 */
public class SpeedPayCancel extends PaymentCancelPacket{
    
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}

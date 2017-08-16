/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.messages;

import nfc.messages.base.PaymentRequestPacket;

/**
 *
 * @author Admin
 */
public class SpeedPayRequest extends PaymentRequestPacket{
    
    private String card_no;
    private String card_ymd;
    private String card_serial;
    private String sell_nm;
    private String sign = null;
    private String amt;
    private String product_nm = "";
    private String buyer_nm = "";
    private String buyer_phone_no = "";
    private String buyer_email= "";

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getCard_ymd() {
        return card_ymd;
    }

    public void setCard_ymd(String card_ymd) {
        this.card_ymd = card_ymd;
    }

    public String getCard_serial() {
        return card_serial;
    }

    public void setCard_serial(String card_serial) {
        this.card_serial = card_serial;
    }

    public String getSell_nm() {
        return sell_nm;
    }

    public void setSell_nm(String sell_nm) {
        this.sell_nm = sell_nm;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getProduct_nm() {
        return product_nm;
    }

    public void setProduct_nm(String product_nm) {
        this.product_nm = product_nm;
    }

    public String getBuyer_nm() {
        return buyer_nm;
    }

    public void setBuyer_nm(String buyer_nm) {
        this.buyer_nm = buyer_nm;
    }

    public String getBuyer_phone_no() {
        return buyer_phone_no;
    }

    public void setBuyer_phone_no(String buyer_phone_no) {
        this.buyer_phone_no = buyer_phone_no;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }
    
    
}

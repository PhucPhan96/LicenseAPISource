/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nfc.model;

/**
 *
 * @author Admin
 */
public class Filter {
    int[] supplierId;
    String fromDate;
    String toDate;
    String status;

    public String getStatus() {
        return status;
    }

    public int[] getSupplierId() {
        return supplierId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }
}

// Author: Craig Treulieb 0606138
// Date: Oct 16 2014
// File: PurchaseOrderDTO.java
// Purpose: Purchase Order Data Transfer Object.
package dtos;

import java.io.Serializable;
import java.util.ArrayList;

public class PurchaseOrderDTO implements Serializable{
    public PurchaseOrderDTO(){}
    private double total;
    private ArrayList<PurchaseOrderLineItemDTO> items;
    private int vendorno;
    private String date;
    private int ponumber;

    public int getPonumber() {
        return ponumber;
    }

    public void setPonumber(int ponumber) {
        this.ponumber = ponumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<PurchaseOrderLineItemDTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<PurchaseOrderLineItemDTO> items) {
        this.items = items;
    }

    public int getVendorno() {
        return vendorno;
    }

    public void setVendorno(int vendorno) {
        this.vendorno = vendorno;
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csc335finalproject;

/**
 *
 * @author Max Gillman
 */
public class Order {
    
    
    String orderNumber;
    String menuID;
    String itemDesc;
    String itemSize;   
    String itemPrice;        
    String combo;        

    public Order(String orderNumber, String menuID, String itemDesc, String itemSize, String itemPrice, String combo) {
        this.orderNumber = orderNumber;
        this.menuID = menuID;
        this.itemDesc = itemDesc;
        this.itemSize = itemSize;
        this.itemPrice = itemPrice;
        this.combo = combo;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }
            

    
    
    
    
}

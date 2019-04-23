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
public class Count {
    
    
    String itemID;
    String itemDesc;
    String itemSize;
    String count;

    public Count(String itemID, String itemDesc, String itemSize, String count) {
        this.itemID = itemID;
        this.itemDesc = itemDesc;
        this.itemSize = itemSize;
        this.count = count;
    }
    

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
    
 
    
    
    
}

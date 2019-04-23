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
public class Transaction {
    
    String orderNumber;
    String date;
    String taker;
    String total;

    public Transaction(String orderNumber, String date, String taker, String total) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.taker = taker;
        this.total = total;
    }


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaker() {
        return taker;
    }

    public void setTaker(String taker) {
        this.taker = taker;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    
    
}

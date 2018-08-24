package com.jilian.ccbticketing.Model;

import java.io.Serializable;

public class TicketModel implements Serializable {
    private String ID,name,paytime;
    private Double price;
    private boolean ispay;
    private int isCheck,isRefund;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isIspay() {
        return ispay;
    }

    public void setIspay(boolean ispay) {
        this.ispay = ispay;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(int isRefund) {
        this.isRefund = isRefund;
    }

    public TicketModel(String ID, String name, String paytime, Double price, boolean ispay, int isCheck, int isRefund) {
        this.ID = ID;
        this.name = name;
        this.paytime = paytime;
        this.price = price;
        this.ispay = ispay;
        this.isCheck = isCheck;
        this.isRefund = isRefund;
    }

    public TicketModel() {
    }

    @Override
    public String toString() {
        return "{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", paytime='" + paytime + '\'' +
                ", price=" + price +
                ", ispay=" + ispay +
                ", isCheck=" + isCheck +
                ", isRefund=" + isRefund +
                '}';
    }
}

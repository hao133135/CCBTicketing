package com.jilian.ccbticketing.Model;

import java.io.Serializable;
import java.util.List;

public class SellListModel implements Serializable {
    private String payType,terminalID,payTime,thirdId,phone;
    private List<SellModel> sellModels;
    private List<TicketModel> ticketModels;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<SellModel> getSellModels() {
        return sellModels;
    }

    public void setSellModels(List<SellModel> sellModels) {
        this.sellModels = sellModels;
    }

    public List<TicketModel> getTicketModels() {
        return ticketModels;
    }

    public void setTicketModels(List<TicketModel> ticketModels) {
        this.ticketModels = ticketModels;
    }

    @Override
    public String toString() {
        return "{" +
                "payType='" + payType + '\'' +
                ", terminalID='" + terminalID + '\'' +
                ", payTime='" + payTime + '\'' +
                ", thirdId='" + thirdId + '\'' +
                ", phone='" + phone + '\'' +
                ", sellModels=" + sellModels +
                ", ticketModels=" + ticketModels +
                '}';
    }
}

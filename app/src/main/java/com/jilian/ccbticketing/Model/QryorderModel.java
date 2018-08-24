package com.jilian.ccbticketing.Model;

import java.io.Serializable;
import java.util.List;

public class QryorderModel implements Serializable {
    private String payOrder;
    private List<TicketModel> ticketModels;
    private List<GuideModel> guideModels;

    public QryorderModel() {
    }

    public QryorderModel(String payOrder, List<TicketModel> ticketModels, List<GuideModel> guideModels) {
        this.payOrder = payOrder;
        this.ticketModels = ticketModels;
        this.guideModels = guideModels;
    }

    public String getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(String payOrder) {
        this.payOrder = payOrder;
    }

    public List<TicketModel> getTicketModels() {
        return ticketModels;
    }

    public void setTicketModels(List<TicketModel> ticketModels) {
        this.ticketModels = ticketModels;
    }

    public List<GuideModel> getGuideModels() {
        return guideModels;
    }

    public void setGuideModels(List<GuideModel> guideModels) {
        this.guideModels = guideModels;
    }

    @Override
    public String toString() {
        return "QryorderModel{" +
                "payOrder='" + payOrder + '\'' +
                ", ticketModels=" + ticketModels +
                ", guideModels=" + guideModels +
                '}';
    }
}

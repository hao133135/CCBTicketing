package com.jilian.ccbticketing.Model;

import java.io.Serializable;

public class DetailsModel implements Serializable {
    private String ticketOne,ticketTwo;

    public String getTicketOne() {
        return ticketOne;
    }

    public void setTicketOne(String ticketOne) {
        this.ticketOne = ticketOne;
    }

    public String getTicketTwo() {
        return ticketTwo;
    }

    public void setTicketTwo(String ticketTwo) {
        this.ticketTwo = ticketTwo;
    }

    @Override
    public String toString() {
        return "DetailsModel{" +
                "ticketOne='" + ticketOne + '\'' +
                ", ticketTwo='" + ticketTwo + '\'' +
                '}';
    }

    public DetailsModel(String ticketOne, String ticketTwo) {
        this.ticketOne = ticketOne;
        this.ticketTwo = ticketTwo;
    }

    public DetailsModel() {
    }
}

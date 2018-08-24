package com.jilian.ccbticketing.Model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class SellModel implements Serializable {
    private String id,name;
    private int num;
    private double price,realPrice,total;
    private boolean isSelect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price =Double.valueOf( doubleToString(price));
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = Double.valueOf( doubleToString(realPrice));
    }

    public double getTotal() {
        return this.getNum()*this.getRealPrice();
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public SellModel(String id, String name, double price, double realPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.realPrice = realPrice;
    }

    public SellModel() {
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", price=" + price +
                ", realPrice=" + realPrice +
                ", total=" + getTotal() +
                '}';
    }

    public SellModel(String id, String name, int num, double price, double realPricect) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.price = price;
    }

    /**
     * double转String,保留小数点后两位
     * @param num
     * @return
     */
    public static String doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

}

package com.jilian.ccbticketing.Model;

import java.io.Serializable;

public class GuideModel implements Serializable{
    private String name,id,addr;

    public GuideModel() {
    }

    public GuideModel(String name, String id, String addr) {
        this.name = name;
        this.id = id;
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "GuideModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}

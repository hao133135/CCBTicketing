package com.jilian.ccbticketing.Model;

import java.io.Serializable;

public class SettingModel implements Serializable {
    private String ip,port;
    private boolean isCancellation;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isCancellation() {
        return isCancellation;
    }

    public void setCancellation(boolean cancellation) {
        isCancellation = cancellation;
    }

    @Override
    public String toString() {
        return "SettingModel{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", isCancellation=" + isCancellation +
                '}';
    }
}

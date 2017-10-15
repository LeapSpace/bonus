package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class EventLocation extends MessageBase {
    private final static String msgType = "event";
    private final static String event = "LOCATION";
    private double latitude;
    private double longitude;
    private double precision;

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getEvent() {
        return event;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }
}

package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class MessageLocation extends MessageBase {
    private final static String msgType = "location";
    private Double location_X;
    private Double location_Y;
    private float scale;
    private String label;
    private String msgId;


    @Override
    public String getMsgType() {
        return msgType;
    }

    public Double getLocation_X() {
        return location_X;
    }

    public void setLocation_X(Double location_X) {
        this.location_X = location_X;
    }

    public Double getLocation_Y() {
        return location_Y;
    }

    public void setLocation_Y(Double location_Y) {
        this.location_Y = location_Y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

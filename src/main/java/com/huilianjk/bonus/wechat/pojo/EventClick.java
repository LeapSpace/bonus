package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class EventClick extends MessageBase {
    private final static String msgType = "event";
    private final static String event = "CLICK";
    private String eventKey;

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getEvent() {
        return event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}

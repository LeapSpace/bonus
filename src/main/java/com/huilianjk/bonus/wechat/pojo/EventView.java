package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/4/7.
 */
public class EventView extends MessageBase {
    private final static String msgType = "event";
    private final static String event = "view";
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

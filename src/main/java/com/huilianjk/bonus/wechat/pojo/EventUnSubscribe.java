package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class EventUnSubscribe extends MessageBase {
    private final static String msgType = "event";
    private final static String event = "unsubscribe";

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getEvent() {
        return event;
    }
}

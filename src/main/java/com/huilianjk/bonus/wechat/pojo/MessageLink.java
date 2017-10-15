package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class MessageLink extends MessageBase {
    private final static String msgType = "link";
    private String title;
    private String description;
    private String url;
    private String msgId;

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

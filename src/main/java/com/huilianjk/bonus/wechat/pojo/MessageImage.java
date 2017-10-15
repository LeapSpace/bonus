package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class MessageImage extends MessageBase {
    private final static String msgType = "image";
    private String picUrl;
    private String mediaId;
    private String msgId;

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

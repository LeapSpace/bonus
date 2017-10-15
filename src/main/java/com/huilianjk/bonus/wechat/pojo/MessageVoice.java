package com.huilianjk.bonus.wechat.pojo;

/**
 * Created by space on 16/3/8.
 */
public class MessageVoice extends MessageBase {
    private final static String msgType = "voice";
    private String mediaId;
    private String format;
    private String recognition = "";
    private String msgId;

    @Override
    public String getMsgType() {
        return msgType;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

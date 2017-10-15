package com.huilianjk.bonus.wechat.util;

import com.huilianjk.bonus.wechat.pojo.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by space on 16/3/8.
 */
public class XmlParser {
    private final static Logger logger = LoggerFactory.getLogger(XmlParser.class);

    public static MessageBase parserXml(String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            return MessageFactory(root);
        } catch (DocumentException de) {
            logger.info("xml parse error", de);
        }
        return null;
    }

    private static MessageBase MessageFactory(Element element) {
        String msgType = element.element("MsgType").getText();
        MessageBase msg = null;
        switch (msgType) {
            case "text":
                msg = handleMsgText(element);
                break;
            case "image":
                msg = handleMsgImage(element);
                break;
            case "voice":
                msg = handleMsgVoice(element);
                break;
            case "video":
                msg = handleMsgVideo(element);
                break;
            case "shortvideo":
                msg = handleMsgShortVideo(element);
                break;
            case "location":
                msg = handleMsgLocation(element);
                break;
            case "link":
                msg = handleMsgLink(element);
                break;
            case "event":
                msg = handleEvent(element);
                break;
            default:
                break;
        }
        if (msg == null) {
            return null;
        }
        msg.setMsgType(msgType);
        msg.setCreateTime(Long.parseLong(element.element("CreateTime").getText()));
        msg.setFromUserName(element.element("FromUserName").getText());
        msg.setToUserName(element.element("ToUserName").getText());
        return msg;
    }

    public static MessageBase handleMsgText(Element element) {
        MessageText msg = new MessageText();
        msg.setContent(element.element("Content").getText());
        msg.setMsgId(element.element("MsgId").getText());
        return msg;
    }

    public static MessageBase handleMsgImage(Element element) {
        MessageImage msg = new MessageImage();
        msg.setMediaId(element.element("MediaId").getText());
        msg.setPicUrl(element.element("PicUrl").getText());
        msg.setMsgId(element.element("MsgId").getText());
        return msg;
    }

    public static MessageBase handleMsgVoice(Element element) {
        MessageVoice msg = new MessageVoice();
        msg.setMediaId(element.element("MediaId").getText());
        msg.setFormat(element.element("Format").getText());
        msg.setMsgId(element.element("MsgId").getText());
        if (element.element("Recognition").getText() != null) {
            msg.setRecognition(element.element("Recognition").getText());
        }
        return msg;
    }

    public static MessageBase handleMsgVideo(Element element) {
        MessageVideo msg = new MessageVideo();
        msg.setMediaId(element.element("MediaId").getText());
        msg.setThumbMediaId(element.element("ThumbMediaId").getText());
        msg.setMsgId(element.element("MsgId").getText());
        return msg;
    }


    public static MessageBase handleMsgShortVideo(Element element) {
        MessageShortVideo msg = new MessageShortVideo();
        msg.setMediaId(element.elementText("MediaId"));
        msg.setThumbMediaId(element.elementText("ThumbMediaId"));
        msg.setMsgId(element.element("MsgId").getText());
        return msg;
    }

    public static MessageBase handleMsgLocation(Element element) {
        MessageLocation msg = new MessageLocation();
        msg.setLabel(element.elementText("Label"));
        msg.setLocation_X(Double.valueOf(element.elementText("Location_X")));
        msg.setLocation_Y(Double.valueOf(element.elementText("Location_Y")));
        msg.setScale(Float.valueOf(element.elementText("Scale")));
        msg.setMsgId(element.element("MsgId").getText());
        return msg;
    }

    public static MessageBase handleMsgLink(Element element) {
        MessageLink msg = new MessageLink();
        msg.setTitle(element.elementText("Title"));
        msg.setUrl(element.elementText("Url"));
        msg.setDescription(element.elementText("Description"));
        msg.setMsgId(element.element("MsgId").getText());
        return msg;
    }

    public static MessageBase handleEvent(Element element) {
        if (element.element("Event").getText().equals("subscribe")) {
            //关注
            EventSubscribe msg = new EventSubscribe();
            if (element.element("EventKey") != null) {
                logger.debug(element.element("EventKey").getText());
                msg.setEventKey(element.element("EventKey").getText());
            }
            if (element.element("Ticket") != null) {
                msg.setTicket(element.element("Ticket").getText());
            }
            return msg;
        } else if (element.element("Event").getText().equals("unsubscribe")) {
            //取消关注
            EventUnSubscribe msg = new EventUnSubscribe();
            return msg;
        } else if (element.element("Event").getText().equals("SCAN")) {
            // 扫码
            EventScan msg = new EventScan();
            msg.setEventKey(element.element("EventKey").getText());
            msg.setTicket(element.element("Ticket").getText());
            return msg;
        } else if (element.element("Event").getText().equals("LOCATION")) {
            // 上传地理位置
            EventLocation msg = new EventLocation();
            msg.setLatitude(Double.valueOf(element.element("Latitude").getText()));
            msg.setLongitude(Double.valueOf(element.element("Longitude").getText()));
            msg.setPrecision(Double.valueOf(element.element("Precision").getText()));
            return msg;
        } else if (element.element("Event").getText().equals("CLICK")) {
            //自定义菜单
            EventClick msg = new EventClick();
            msg.setEventKey(element.element("EventKey").getText());
            return msg;
        } else if (element.element("Event").getText().equals("VIEW")) {
            //自定义菜单
            EventView msg = new EventView();
            msg.setEventKey(element.element("EventKey").getText());
            return msg;
        }
        return null;
    }
}

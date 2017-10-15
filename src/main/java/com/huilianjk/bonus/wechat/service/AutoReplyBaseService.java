package com.huilianjk.bonus.wechat.service;

import com.huilianjk.bonus.wechat.pojo.MessageBase;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by space on 16/3/8.
 */
public abstract class AutoReplyBaseService implements AutoReplyService {

    private final Logger logger = LoggerFactory.getLogger(AutoReplyBaseService.class);

    public abstract Document reply(MessageBase msg);

    public static Document genSingleTuwenMsg(MessageBase msg, String title, String description, String picurl, String url) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("xml");
        root.addElement("ToUserName").addCDATA(msg.getFromUserName());
        root.addElement("FromUserName").addCDATA(msg.getToUserName());
        root.addElement("CreateTime").addText(String.valueOf(new Date().getTime() / 1000));
        root.addElement("MsgType").addCDATA("news");
        root.addElement("ArticleCount").addText("1");
        Element articles = root.addElement("Articles");
        Element item = articles.addElement("item");
        item.addElement("Title").addCDATA(title);
        item.addElement("Description").addCDATA(description);
        item.addElement("PicUrl").addCDATA(picurl);
        item.addElement("Url").addCDATA(url);
        return document;
    }

    public static Document genTextMsg(MessageBase msg, String text) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        Element root = document.addElement("xml");
        root.addElement("ToUserName").addCDATA(msg.getFromUserName());
        root.addElement("FromUserName").addCDATA(msg.getToUserName());
        root.addElement("CreateTime").addText(String.valueOf(new Date().getTime() / 1000));
        root.addElement("MsgType").addCDATA("text");
        root.addElement("Content").addCDATA(text);
        return document;
    }

}

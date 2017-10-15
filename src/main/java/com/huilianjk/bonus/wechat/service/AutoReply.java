package com.huilianjk.bonus.wechat.service;

import com.huilianjk.bonus.wechat.pojo.MessageBase;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by space on 16/10/19.
 */
@Service
public class AutoReply extends AutoReplyBaseService {

    private static final Logger logger = LoggerFactory.getLogger(AutoReply.class);

    public Document reply(MessageBase msg) {
        Document document = null;
//        if (msg instanceof MessageText) {
//            if (((MessageText) msg).getContent().equals("理财师")) {
//                String text = "请选择您需要的服务：\n\n" +
//                        "1.<a href=\"http://" + domain + "/wx?target=/webview/wechat/customer_list\">查看我的客户</a>\n\n" +
//                        "2.<a href=\"http://" + domain + "/wx?target=/webview/wechat/product_list\">查看我的业绩</a>\n\n" +
//                        "注：微信绑卡实名后才能收到佣金哦";
//                document = genTextMsg(msg, text);
//            }
//        } else if (msg instanceof MessageVoice) {
//            if (((MessageVoice) msg).getRecognition().contains("理财师")) {
//                String text = "请选择您需要的服务：\n\n" +
//                        "1.<a href=\"http://" + domain + "/wx?target=/webview/wechat/customer_list\">查看我的客户</a>\n\n" +
//                        "2.<a href=\"http://" + domain + "/wx?target=/webview/wechat/product_list\">查看我的业绩</a>\n\n" +
//                        "注：微信绑卡实名后才能收到佣金哦";
//                document = genTextMsg(msg, text);
//            }
//        }
        return document;
    }
}

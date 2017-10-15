package com.huilianjk.bonus.wechat.service;

import com.huilianjk.bonus.wechat.pojo.MessageBase;
import org.dom4j.Document;

/**
 * Created by space on 16/4/25.
 */
public interface AutoReplyService {
    Document reply(MessageBase msg);
}

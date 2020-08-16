package dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TextMessage {
    /**
     * 开发者微信号
     */
    @JSONField(name = "ToUserName")
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    @JSONField(name = "FromUserName")
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    @JSONField(name = "CreateTime")
    private String createTime;

    /**
     * MsgType	消息类型，文本为text
     */
    @JSONField(name = "MsgType")
    private String msgType;

    /**
     * 文本消息内容
     */
    @JSONField(name = "Content")
    private String content;

    /**
     * 消息id，64位整型
     */
    @JSONField(name = "MsgId")
    private String msgId;

    public String toXml() {
        return "<xml>\n" +
                "    <ToUserName><![CDATA["+toUserName+"]]></ToUserName>\n" +
                "    <FromUserName><![CDATA["+fromUserName+"]]></FromUserName>\n" +
                "    <CreateTime>"+createTime+"</CreateTime>\n" +
                "    <MsgType><![CDATA["+msgType+"]]></MsgType>\n" +
                "    <Content><![CDATA["+content+"]]></Content>\n" +
                "    <MsgId>"+msgId+"</MsgId>\n" +
                "</xml>";
    }
}

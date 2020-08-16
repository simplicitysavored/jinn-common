import dto.TextMessage;
import xyz.yuanjin.project.common.util.XmlUtil;

public class XmlTest {
    public static void main(String[] args) throws Exception {
        String xml = "<xml>\n" +
                "  <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "  <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "  <CreateTime>1348831860</CreateTime>\n" +
                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                "  <Content><![CDATA[this is a test]]></Content>\n" +
                "  <MsgId>1234567890123456</MsgId>\n" +
                "</xml>";

        System.out.println(XmlUtil.parseXmlOfWx(xml, TextMessage.class).toXml());
    }
}

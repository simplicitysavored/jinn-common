package xyz.yuanjin.project.common.util;

import com.alibaba.fastjson.annotation.JSONField;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;

public class XmlUtil {
    private static final String PRE = "<![CDATA[";
    private static final String END = "]]>";

    public static <T, J> T parseXmlOfWx(String xmlString, Class<T> destClz) throws IllegalAccessException, InstantiationException, DocumentException {

        return parseXmlOfWx(xmlString, destClz, false);
    }

    public static <T, J> T parseXmlOfWx(String xmlString, Class<T> destClz, boolean format) throws IllegalAccessException, InstantiationException, DocumentException {
        T item = destClz.newInstance();

        Document document = DocumentHelper.parseText(xmlString);

        // 获取根节点
        Element xml = document.getRootElement();

        Field[] fields = destClz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            JSONField jsonField = field.getAnnotation(JSONField.class);
            if (null != jsonField) {
                Element el = xml.element(jsonField.name());
                if (format) {
                    String textTrim = el.getTextTrim();
                    if (textTrim.startsWith(PRE) && textTrim.endsWith(END)) {
                        System.out.println("需要转换");
                    }
                }
                field.set(item, el.getText());
            }
        }

        return item;
    }
}

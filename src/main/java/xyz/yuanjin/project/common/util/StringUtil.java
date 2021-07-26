package xyz.yuanjin.project.common.util;

/**
 * @author yuanjin
 */
public class StringUtil {

    /**
     * 是否为空（null）
     * @param value String
     * @return {true|false}
     */
    public static Boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 是否不为空（null）
     * @param value String
     * @return {true|false}
     */
    public static Boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 是否所有字符串均不为空（null）
     * @param value String Arrays
     * @return {true|false}
     */
    public static Boolean isAllNotEmpty(String... value) {
        for (String tmp : value) {
            if (isEmpty(tmp)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否不为null，且是空白字符串
     * @param value String
     * @return {true|false}
     */
    public static Boolean isBlank(String value) {
        return isEmpty(value) || value.trim().length() == 0;
    }

    public static Boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static String ifNull(String value, String replace) {
        if (null == value) {
            return replace;
        }
        return value;
    }
}

package xyz.yuanjin.project.common.util;

import java.util.Arrays;

/**
 * 数组相关的工具
 *
 * @author yuanjin
 */
public class ArrayUtils {

    /**
     * 是否为空（null）
     *
     * @param value String
     * @return {true|false}
     */
    private static Boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 是否不为空（null）
     *
     * @param value String
     * @return {true|false}
     */
    private static Boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 是否不为null，且是空白字符串
     *
     * @param value String
     * @return {true|false}
     */
    private static Boolean isBlank(String value) {
        return isNotEmpty(value) && value.trim().length() == 0;
    }

    private static Boolean isNotBlank(String value) {
        return !isBlank(value);
    }


    /**
     * 数组转字符串
     *
     * @param strArray  数组
     * @param delimiter 分隔符
     * @return String
     */
    public static String join(String[] strArray, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            sb.append(strArray[i]);
            if (i < strArray.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static boolean isBlank(String[] strArray) {
        if (null != strArray) {
            if (strArray.length > 0) {
                for (String str : strArray) {
                    if (isNotBlank(str)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isNotBlank(String[] strArray) {
        return !isBlank(strArray);
    }

    public static String[] add(String[] oldArray, String value) {
        int newLength = oldArray.length + 1;
        String[] newArray = Arrays.copyOf(oldArray, newLength);
        newArray[newLength - 1] = value;
        return newArray;
    }

    public static void main(String[] args) {
        String[] tmp = {"1","2"};
        String[] newArr = add(tmp, "3");
        System.out.println(newArr);
    }


}

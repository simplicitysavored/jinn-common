package xyz.yuanjin.project.common.util;

/**
 * 数组相关的工具
 *
 * @author yuanjin
 */
public class ArrayUtils {
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

}

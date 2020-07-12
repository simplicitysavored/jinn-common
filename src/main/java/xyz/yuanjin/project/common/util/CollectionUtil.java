package xyz.yuanjin.project.common.util;

import java.util.List;
import java.util.Map;

/**
 * @author yuanjin
 */
public class CollectionUtil {

    public static boolean isEmpty(Map value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmpty(Map value) {
        return !isEmpty(value);
    }


    public static String listToString(List<String> list, String separator){
        if (list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list){
            sb.append(str).append(separator);
        }
        return sb.substring(0, sb.length()-separator.length()).trim();
    }

}

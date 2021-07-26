package xyz.yuanjin.project.common.util;

/**
 * @author yuanjin
 */
public class Helper {
    public static final String UPPER_A_Z = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String LOWER_A_Z = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        System.out.println(formatMills(435*1000L));
//        for (int i = 0; i < 100; i++) {
//            System.out.println(Math.random());
//        }
//        for (int i = 65; i < 65 + 26; i++) {
//            System.out.print((char) i);
//        }
//        System.out.println();
//        for (int i = 97; i < 123; i++) {
//            System.out.print((char) i);
//        }
//        System.out.println();
    }

    public static String radonString(int length) {
        return null;
    }

    public static String formatMills(long mills) {
        long tmpMills = mills;
        String retFmt = "";
        if (tmpMills > 60 * 60 * 1000L) {
            retFmt += (tmpMills / (60 * 60 * 1000L)) + "时";
            tmpMills %= (60 * 60 * 1000L);
        }
        if (tmpMills > 60 * 1000L) {
            retFmt += (tmpMills / (60 * 1000L)) + "分";
            tmpMills %= (60 * 1000L);
        }
        if (tmpMills > 1000L) {
            retFmt += (tmpMills / (1000L)) + "秒";
            tmpMills %= (1000L);
        }
        if (tmpMills > 0) {
            retFmt += tmpMills+"ms";
        }
        return retFmt;
    }

}

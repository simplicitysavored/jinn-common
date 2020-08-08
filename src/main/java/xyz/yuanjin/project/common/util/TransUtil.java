package xyz.yuanjin.project.common.util;

import xyz.yuanjin.project.common.enums.UnitEnum;

import java.text.DecimalFormat;

/**
 * 转换/翻译工具
 */
public class TransUtil {

    /**
     * 转换单位
     *
     * @param value 需要转换的值
     * @param from  单位（需要转换的值）
     * @param to    单位（结果单位）
     * @return String（带单位）
     */
    public static String convert(Long value, UnitEnum from, UnitEnum to) {
        return convert(value * 1.0, from, to);
    }

    public static String convert(Double value, UnitEnum from, UnitEnum to) {
        String dfString = "##.##".concat(to.getEnglishNm());
        DecimalFormat df = new DecimalFormat(dfString);
        return df.format(value * from.getRate() / to.getRate());
    }

    /**
     * 自动转换单位
     *
     * @param bytes 字节数
     * @return 转换后的字节数（String）
     */
    public static String convertTrafficAuto(Double bytes) {
        if (bytes / UnitEnum.TB.getRate() > UnitEnum.TB.getRate()) {
            return convert(bytes, UnitEnum.B, UnitEnum.TB);
        } else if (bytes > UnitEnum.GB.getRate()) {
            return convert(bytes, UnitEnum.B, UnitEnum.GB);
        } else if (bytes > UnitEnum.MB.getRate()) {
            return convert(bytes, UnitEnum.B, UnitEnum.MB);
        } else if (bytes > UnitEnum.KB.getRate()) {
            return convert(bytes, UnitEnum.B, UnitEnum.KB);
        } else {
            return convert(bytes, UnitEnum.B, UnitEnum.B);
        }
    }

    /**
     * 自动转换单位
     *
     * @param bytes 字节数
     * @return 转换后的字节数（String）
     */
    public static String convertTrafficAuto(Long bytes) {
        return convertTrafficAuto(bytes * 1.0);
    }

}

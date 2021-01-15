package xyz.yuanjin.project.common.enums;

/**
 * @author yuanjin
 */

public enum DateFormatEnum {
    /**
     * 日期格式
     */
    YYYY_MM_DD_HH_MM_SS_1("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_SS_2("yyyy/MM/dd HH:mm:ss"),
    YYYY_MM_DD_HH_MM_1("yyyy-MM-dd HH:mm"),
    YYYY_MM_DD_HH_MM_2("yyyy/MM/dd HH:mm"),
    YYYY_MM_DD_1("yyyy-MM-dd"),
    YYYY_MM_DD_2("yyyy/MM/dd"),
    HH_MM_SS("HH:mm:ss"),
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    YYYY_MM_DD_HH_MM_ZH("yyyy年MM月dd日 HH时mm分ss秒");

    private String value;

    DateFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

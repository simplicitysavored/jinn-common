package xyz.yuanjin.project.common.enums;

import lombok.Getter;

/**
 * @author yuanjin
 */
@Getter
public enum UnitEnum {
    /**
     * 流量单位
     */
    B("B", "B", 1.0),
    KB("KB", "KB", 1024.0),
    MB("MB", "MB", 1024.0 * 1024.0),
    GB("GB", "GB", 1024.0 * 1024.0 * 1024.0),
    TB("TB", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0);


    /**
     * 英文名
     */
    private String englishNm;
    /**
     * 中文名
     */
    private String chineseNm;

    private Double rate;

    UnitEnum(String englishNm, String chineseNm, Double rate) {
        this.englishNm = englishNm;
        this.chineseNm = chineseNm;
        this.rate = rate;
    }


}

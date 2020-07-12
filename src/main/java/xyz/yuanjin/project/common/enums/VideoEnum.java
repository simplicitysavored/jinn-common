package xyz.yuanjin.project.common.enums;

import java.util.regex.Pattern;

/**
 * @author yuanjin
 */
public enum VideoEnum {
    /**
     * 后缀
     */
    MP4("mp4"),
    FLV("flv");

    private String suffix;
    private Pattern pattern;

    VideoEnum(String suffix) {
        this.suffix = suffix;
        pattern = Pattern.compile("^.*\\.".concat(suffix).concat("$"));
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}

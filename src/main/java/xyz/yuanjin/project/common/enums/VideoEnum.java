package xyz.yuanjin.project.common.enums;

import java.util.regex.Pattern;

/**
 * @author yuanjin
 */
public enum VideoEnum {
    /**
     * 微软视频 ：wmv、asf、asx
     * Real Player ：rm、 rmvb
     * MPEG视频 ：mp4
     * 手机视频 ：3gp
     * Apple视频 ：mov、m4v
     * 其他常见视频：avi、dat、mkv、flv、vob等
     */
    WMV("wmv"),
    ASF("asf"),
    ASX("asx"),
    RM("rm"),
    RMVB("rmvb"),
    MP4("mp4"),
    $3GP("3gp"),
    MOV("mov"),
    M4V("m4v"),
    AVI("avi"),
    DAT("dat"),
    MKV("mkv"),
    VOB("vob"),
    FLV("flv");

    private String suffix;
    private Pattern pattern;

    VideoEnum(String suffix) {
        this.suffix = suffix;
        String regex = "^.*\\.".concat(suffix).concat("$");
        // 忽略字母大小写
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
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

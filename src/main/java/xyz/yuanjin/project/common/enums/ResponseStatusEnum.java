package xyz.yuanjin.project.common.enums;

/**
 * @author yuanjin
 */
public enum ResponseStatusEnum {
    /**
     * 响应状态
     */
    SUCCESS(200, "成功"),
    PARAMS_ERROR(400, "参数不符合要求"),
    SERVER_ERROR(500, "服务端错误");


    private int code;
    private String desc;

    ResponseStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

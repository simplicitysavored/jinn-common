package xyz.yuanjin.project.common.dto;

import com.alibaba.fastjson.JSON;

/**
 * @author yuanjin
 */
public class ResponseDTO {

    /**
     * 返回状态码
     */
    private Integer statusCode;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 主要数据
     */
    private Object data;

    /**
     * 扩展字段
     */
    private Object extendInfo;

    public Integer getStatusCode() {
        return statusCode;
    }

    public ResponseDTO setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResponseDTO setData(Object data) {
        this.data = data;
        return this;
    }

    public Object getExtendInfo() {
        return extendInfo;
    }

    public ResponseDTO setExtendInfo(Object extendInfo) {
        this.extendInfo = extendInfo;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

package xyz.yuanjin.project.common.dto;

/**
 * @author yuanjin
 */
public class ResponseDTO {
    private boolean success;

    private Integer statusCode;

    private String message;

    private Object data;

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

    public boolean isSuccess() {
        return success;
    }

    public ResponseDTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
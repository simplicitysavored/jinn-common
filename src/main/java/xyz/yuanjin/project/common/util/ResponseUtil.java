package xyz.yuanjin.project.common.util;

import com.alibaba.fastjson.JSON;
import xyz.yuanjin.project.common.dto.ResponseDTO;
import xyz.yuanjin.project.common.enums.ResponseEnum;

/**
 * @author yuanjin
 */
public class ResponseUtil {

    public static ResponseDTO response(ResponseEnum value) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(value.getCode());
        dto.setMessage(value.getDesc());
        dto.setSuccess(ResponseEnum.SUCCESS.equals(value));
        return dto;
    }

    public static ResponseDTO response(ResponseEnum value, Object data) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(value.getCode());
        dto.setMessage(value.getDesc());
        dto.setSuccess(ResponseEnum.SUCCESS.equals(value));
        dto.setData(data);
        return dto;
    }

    public static ResponseDTO response(ResponseEnum value, String message) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(value.getCode());
        dto.setMessage(message);
        dto.setSuccess(ResponseEnum.SUCCESS.equals(value));
        return dto;
    }

    public static ResponseDTO response(ResponseEnum value, Object data, String message) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(value.getCode());
        dto.setMessage(message);
        dto.setSuccess(ResponseEnum.SUCCESS.equals(value));
        dto.setData(data);
        return dto;
    }

    @Deprecated
    public static ResponseDTO error(Integer statusCode, String errorMsg) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(statusCode);
        dto.setSuccess(false);
        dto.setMessage(errorMsg);

        return dto;
    }

    @Deprecated
    public static ResponseDTO success(Integer statusCode, String successMsg) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(statusCode);
        dto.setSuccess(true);
        dto.setMessage(successMsg);

        return dto;
    }

    @Deprecated
    public static ResponseDTO success() {
        return success(200, null);
    }

    @Deprecated
    public static ResponseDTO error(String errorMsg) {
        return error(500, errorMsg);
    }

    @Deprecated
    public static String successString() {
        return JSON.toJSONString(success());
    }

    @Deprecated
    public static String errorString(String errorMsg) {
        return JSON.toJSONString(error(errorMsg));
    }


}

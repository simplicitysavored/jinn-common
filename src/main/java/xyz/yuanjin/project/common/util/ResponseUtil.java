package xyz.yuanjin.project.common.util;

import com.alibaba.fastjson.JSON;
import xyz.yuanjin.project.common.dto.ResponseDTO;
import xyz.yuanjin.project.common.enums.ResponseEnum;

/**
 * @author yuanjin
 */
public class ResponseUtil {

    public static ResponseDTO error() {
        return error(null);
    }

    public static ResponseDTO error(String message) {
        return response(ResponseEnum.SERVER_ERROR, null, message, null);
    }

    public static ResponseDTO success() {
        return response(ResponseEnum.SUCCESS, null, null, null);
    }

    public static ResponseDTO response(ResponseEnum value) {
        return response(value, null, null, null);
    }

    public static ResponseDTO response(ResponseEnum value, Object data) {
        return response(value, data, null, null);
    }

    public static ResponseDTO response(ResponseEnum value, String message) {
        return response(value, null, message, null);
    }

    public static ResponseDTO response(ResponseEnum value, Object data, String message) {
        return response(value, data, message, null);
    }

    public static ResponseDTO response(ResponseEnum value, Object data, String message, Object extendInfo) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(value.getCode());
        dto.setMessage(message);
        dto.setData(data);
        dto.setExtendInfo(extendInfo);
        return dto;
    }

    @Deprecated
    public static ResponseDTO error(Integer statusCode, String errorMsg) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(statusCode);
        dto.setMessage(errorMsg);

        return dto;
    }

    @Deprecated
    public static ResponseDTO success(Integer statusCode, String successMsg) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(statusCode);
        dto.setMessage(successMsg);

        return dto;
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

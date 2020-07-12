package xyz.yuanjin.project.common.util;

import com.alibaba.fastjson.JSON;
import xyz.yuanjin.project.common.dto.ResponseDTO;

/**
 * @author yuanjin
 */
public class ResponseUtil {

    public static ResponseDTO error(Integer statusCode, String errorMsg) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(statusCode);
        dto.setSuccess(false);
        dto.setMessage(errorMsg);

        return dto;
    }

    public static ResponseDTO success(Integer statusCode, String successMsg) {
        ResponseDTO dto = new ResponseDTO();
        dto.setStatusCode(statusCode);
        dto.setSuccess(true);
        dto.setMessage(successMsg);

        return dto;
    }

    public static ResponseDTO success() {
        return success(200, null);
    }

    public static ResponseDTO error(String errorMsg) {
        return error(500, errorMsg);
    }

    public static String successString() {
        return JSON.toJSONString(success());
    }

    public static String errorString(String errorMsg) {
        return JSON.toJSONString(error(errorMsg));
    }


}

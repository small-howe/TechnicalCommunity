package com.tangwh.dto;

import com.tangwh.exception.CustomizeErrorCode;
import com.tangwh.exception.CustomizeException;
import lombok.Data;


@Data
public class ResultDTO<T> {
    private Integer code; // 提示信息ID
    private String message; //提示信息
    private T data;
    public static ResultDTO errOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
    public static ResultDTO errOf(CustomizeErrorCode errorCode) {
        return errOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO errOf(CustomizeException e) {

        return errOf(e.getCode(), e.getMessage());
    }
    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;

    }
    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;

    }

}

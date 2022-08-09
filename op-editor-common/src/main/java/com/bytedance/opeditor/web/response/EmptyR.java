package com.bytedance.opeditor.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 空的http响应，不携带data数据。
 *
 * @author fordring
 * @since 2021/4/7
 */
@Data
@Accessors(chain = true)
@Schema(name = "空JSON响应",description = "空的http-JSON响应，不携带data数据。")
public class EmptyR{
    @Schema(name = "HTTP状态码",description = "它应该与HttpHeader的状态码相同。", example = "200")
    protected int code;
    @Schema(name = "响应描述", description = "应当为一串能够提示用户的字符串'", example = "请求成功")
    protected String message;
    @Schema(name = "错误", description = "如果请求出错，将提示错误的名字", example = "权限不足")
    protected String error;

    public EmptyR(){
        setMessage("请求成功");
        setCode(200);
    }

    public static EmptyR successEmpty(){
        return successEmpty(null);
    }

    public static EmptyR successEmpty(String msg){
        EmptyR r = new EmptyR();
        r.setMessage(msg);
        return r;
    }

    public static EmptyR error(int status, String error, String msg){
        EmptyR r = new EmptyR();
        r.setCode(status)
                .setError(error)
                .setMessage(msg);
        return r;
    }

    public static EmptyR error(int status, String error){
        return error(status,error,"请求失败！");
    }

    public static EmptyR error(int status){
        return error(status,"服务端异常");
    }

}

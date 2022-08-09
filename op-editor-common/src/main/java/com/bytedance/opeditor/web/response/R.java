package com.bytedance.opeditor.web.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 携带数据的响应json格式。
 *
 * @param <T> data类型，这里主要用于swagger进行类型判断
 * @author fordring
 * @since 2021/4/7
 */
@Schema(name="标准响应")
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class R <T> extends EmptyR {
    protected T data;

    public static <T> R<T> success(T data){
        R<T> r = new R<>();
        r.setData(data);
        return r;
    }

    public static <T> R<T> errorWithData(int status, String error, String msg, T data){
        R<T> r = new R<>();
        r.setCode(status)
                .setError(error)
                .setMessage(msg);
        r.setData(data);
        return r;
    }

    public static <T> R<T> errorWithData(int status, String error, T data){
        return errorWithData(status,error,"请求失败！",data);
    }

    public static <T> R<T> errorWithData(int status, T data){
        return errorWithData(status,"服务端异常",data);
    }
}


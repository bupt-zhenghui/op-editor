package com.bytedance.opeditor.web;

import com.bytedance.opeditor.utils.WebContext;
import com.bytedance.opeditor.web.response.EmptyR;
import com.bytedance.opeditor.web.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author fordring
 * @since 2020/7/15
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements InitializingBean {

    @ExceptionHandler(value = HttpClientErrorException.class)
    public EmptyR handleHttpClientErrorException(HttpClientErrorException e, HttpServletResponse response) {
        log.warn("[{}]失败[{} {}]:{}", WebContext.fmtReq(),e.getStatusCode().value(),
                e.getStatusCode().getReasonPhrase(), e.getStatusText());
        log.debug("client error", e);
        response.setStatus(e.getRawStatusCode());
        return R.error(e.getRawStatusCode(), e.getStatusCode().getReasonPhrase(), e.getStatusText());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class,
            ValidationException.class,
            MissingServletRequestParameterException.class})
    public EmptyR handleIllegalArgumentException(HttpServletResponse response, Exception e) {
        dealWithException(e, HttpStatus.BAD_REQUEST.value(), response);
        log.debug("", e);
        return R.error(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getLocalizedMessage());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public EmptyR handleUnsupportedOperationException(HttpServletResponse response, Exception e) {
        log.warn("[{}]失败: 该接口尚未开放({})", WebContext.fmtReq(), e.getMessage());
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        log.debug("", e);
        return R.error(HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), "服务未开放");
    }

    @ExceptionHandler({BindException.class})
    public EmptyR handleBindException(HttpServletResponse response, BindException e) {
        log.debug("", e);
        return handleValidException(response, e.getBindingResult(), e.getClass().getSimpleName());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public EmptyR handleMethodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException e) {
        log.debug("", e);
        return handleValidException(response, e.getBindingResult(), e.getClass().getSimpleName());
    }

    private EmptyR handleValidException(HttpServletResponse response, BindingResult bindingResult, String simpleName) {
        String msg = bindingResult.getAllErrors()
                .stream().map(err-> {
                    if(err instanceof FieldError) {
                        return ((FieldError) err).getField() + err.getDefaultMessage();
                    }
                    return err.getDefaultMessage();
                })
                .collect(Collectors.joining(",\n"));
        log.warn("[{}]失败[{}]:{}", WebContext.fmtReq(),
                simpleName, msg);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return R.error(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), msg);
    }

    @ExceptionHandler(value = Exception.class)
    public EmptyR handleException(Exception e, HttpServletResponse response) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        dealWithException(e, status.value(), response);
        log.error("未知异常", e);
        return R.error(status.value(), status.getReasonPhrase(), e.getLocalizedMessage());

    }
    @ExceptionHandler(value = HttpServerErrorException.class)
    public EmptyR handleHttpServerErrorException(HttpServerErrorException e, HttpServletResponse response) {
        dealWithException(e, e.getRawStatusCode(), response);
        log.error("服务端异常:", e);
        return R.error(e.getRawStatusCode(), e.getStatusText(), e.getLocalizedMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public EmptyR handleNoHandlerFoundException(HttpServletResponse response, NoHandlerFoundException e) {
        log.info("[{}] 404.", WebContext.fmtReq());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return R.error(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "找不到路径:"+e.getRequestURL());
    }


    private void dealWithException(Exception e, int code, HttpServletResponse response) {
        log.warn("[{}]失败[{}]:{}", WebContext.fmtReq(),
                e.getClass().getSimpleName(), e.getLocalizedMessage());
        response.setStatus(code);
    }

    @Override
    public void afterPropertiesSet() {
        log.info("全局异常处理器已就绪");
    }
}

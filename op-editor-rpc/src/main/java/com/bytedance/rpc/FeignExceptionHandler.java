package com.bytedance.rpc;

import com.bytedance.opeditor.utils.WebContext;
import com.bytedance.opeditor.web.response.EmptyR;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author fordring
 * @since 2021/11/24
 */
@Slf4j
@RestControllerAdvice
public class FeignExceptionHandler {
    @Resource
    private ObjectMapper objectMapper;

    @ExceptionHandler(value = {FeignException.FeignClientException.class, FeignException.FeignServerException.class})
    public EmptyR handleHttpClientErrorException(FeignException e, HttpServletResponse response) throws IOException {
        String content = e.contentUTF8();
        log.warn("[{}]远程调用失败[{} {}]:{}", WebContext.fmtReq(),e.status(),
                HttpStatus.valueOf(e.status()).getReasonPhrase(), content);
        response.setStatus(e.status());
        return objectMapper.readValue(content, EmptyR.class);
    }
}

package com.bytedance.opeditor.hello;

import com.bytedance.opeditor.auth.AuthValidator;
import com.bytedance.opeditor.web.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fordring
 * @since 2022/8/9
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Resource
    private AuthValidator authValidator;

    @GetMapping
    public R<String> hello() {
        return R.success("hello~ "+authValidator.currentUser().getName());
    }
}

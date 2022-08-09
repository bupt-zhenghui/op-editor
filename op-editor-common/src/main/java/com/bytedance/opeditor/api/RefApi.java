package com.bytedance.opeditor.api;

import com.bytedance.opeditor.api.dto.ref.SetRefForm;
import com.bytedance.opeditor.domain.RefData;
import com.bytedance.opeditor.web.response.EmptyR;
import com.bytedance.opeditor.web.response.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Tag(name = "指标模块")
@RequestMapping("/user")
@ResponseBody
public interface RefApi {

    R<RefData> findById(String id);

    EmptyR setRef(SetRefForm setRefForm);

    EmptyR setRefs(List<SetRefForm> setRefForms);

    R<RefData> createRef(String value, String type);
}

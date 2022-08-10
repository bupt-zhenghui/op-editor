package com.bytedance.opeditor.api;

import com.bytedance.opeditor.annotation.WithAuthLevel;
import com.bytedance.opeditor.api.dto.ref.CreateComputedForm;
import com.bytedance.opeditor.api.dto.ref.SetRefForm;
import com.bytedance.opeditor.domain.LiteralData;
import com.bytedance.opeditor.domain.RefData;
import com.bytedance.opeditor.web.response.EmptyR;
import com.bytedance.opeditor.web.response.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Tag(name = "指标模块")
@RequestMapping("/ref")
@ResponseBody
public interface RefApi {

    @Operation(summary = "通过RefId查询ref数据") @GetMapping("/{id}")
    R<RefData> findById(@PathVariable(value = "id") String id);

    @Operation(summary = "修改ref值") @PutMapping
    EmptyR setRef(@RequestBody SetRefForm setRefForm);

    @Operation(summary = "批量修改ref值") @PutMapping("/batch")
    EmptyR setRefs(@RequestBody List<SetRefForm> setRefForms);

    @Operation(summary = "创建一个ref") @PostMapping
    R<RefData> createRef(@RequestBody LiteralData literalData);

    @Operation(summary = "创建一个computedRef") @PostMapping("/computed")
    R<RefData> createComputed(@RequestBody CreateComputedForm createComputedForm);
}

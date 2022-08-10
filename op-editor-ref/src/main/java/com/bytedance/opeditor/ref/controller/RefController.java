package com.bytedance.opeditor.ref.controller;

import com.bytedance.opeditor.annotation.WithAuthLevel;
import com.bytedance.opeditor.api.RefApi;
import com.bytedance.opeditor.api.dto.ref.CreateComputedForm;
import com.bytedance.opeditor.api.dto.ref.SetRefForm;
import com.bytedance.opeditor.domain.LiteralData;
import com.bytedance.opeditor.domain.RefData;
import com.bytedance.opeditor.ref.service.RefService;
import com.bytedance.opeditor.web.response.EmptyR;
import com.bytedance.opeditor.web.response.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fordring
 * @since 2022/8/10
 */
@Validated
@RestController
@WithAuthLevel
public class RefController implements RefApi {
    @Resource
    private RefService refService;
    @Override
    public R<RefData> findById(String id) {
        return R.success(refService.findById(id));
    }

    @Override
    public EmptyR setRef(SetRefForm setRefForm) {
        refService.setRef(setRefForm.getId(), setRefForm.getValue());
        return R.successEmpty();
    }

    @Override
    public EmptyR setRefs(List<SetRefForm> setRefForms) {
        setRefForms.forEach(this::setRef);
        return R.successEmpty();
    }

    @Override
    public R<RefData> createRef(LiteralData literalData) {
        return R.success(refService.createRef(literalData.getValue(), literalData.getType()));
    }

    @Override
    public R<RefData> createComputed(CreateComputedForm createComputedForm) {
        return R.success(refService.createComputed(createComputedForm.getFormulaId(), createComputedForm.getType()));
    }
}

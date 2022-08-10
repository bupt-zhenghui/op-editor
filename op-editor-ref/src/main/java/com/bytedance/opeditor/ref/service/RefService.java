package com.bytedance.opeditor.ref.service;

import com.bytedance.opeditor.api.dto.ref.SetRefForm;
import com.bytedance.opeditor.domain.RefData;
import com.bytedance.opeditor.ref.entity.RefEntity;
import com.bytedance.opeditor.ref.mapper.RefMapper;
import com.bytedance.opeditor.utils.BeanMapper;
import icu.fordring.ref.MutRef;
import icu.fordring.ref.Ref;
import icu.fordring.ref.refs.ComputedRef;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author fordring
 * @since 2022/8/10
 */
@Service
public class RefService {
    @Resource
    private RefMapper refMapper;
    @Resource
    private ComputedService computedService;

    public RefData findById(String id) {
        return refMapper.findById(id).map(BeanMapper.mapFunc(RefData::new)).orElse(null);
    }

    public void setRef(String id, String value) {
        MutRef<Object> ref = computedService.findMutRef(id);
        if(ref!=null) ref.set(value);
        else throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    public RefData createRef(String value, String type) {
        return BeanMapper.map(doCreateRef(value, type, null), RefData::new);
    }

    public RefData createComputed(String formulaId, String type) {
        RefEntity refEntity = doCreateRef(null, type, formulaId);
        ComputedRef<?> computedRef = computedService.findComputedRef(refEntity);
        computedRef.update(false);
        return BeanMapper.map(refMapper.findById(refEntity.getId()), RefData::new);
    }

    private RefEntity doCreateRef(String value, String type, String formulaId) {
        RefEntity refEntity = new RefEntity();
        refEntity.setValue(value);
        refEntity.setType(type);
        refEntity.setIsComputed(formulaId!=null);
        refEntity.setFormulaId(formulaId);

        refEntity.setCreateTime(new Date());
        refEntity.setUpdateTime(new Date());

        return refMapper.save(refEntity);
    }
}

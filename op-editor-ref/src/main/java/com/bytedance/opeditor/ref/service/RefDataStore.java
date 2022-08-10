package com.bytedance.opeditor.ref.service;

import com.bytedance.opeditor.ref.entity.RefEntity;
import com.bytedance.opeditor.ref.mapper.RefMapper;
import com.bytedance.opeditor.ref.refs.WithId;
import icu.fordring.formula.FormulaParser;
import icu.fordring.ref.Ref;
import icu.fordring.ref.RefStore;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Service
public class RefDataStore implements RefStore {
    @Resource
    private RefMapper refMapper;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(Ref<T> ref) {
        if(ref instanceof WithId withId) {
            String id = withId.id();
            RefEntity refEntity = refMapper.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到id为" + id + "的ref"));
            return (T) FormulaParser.parseLiteral(refEntity.getValue(), refEntity.getType()).value();
        }
        return null;
    }

    @Override
    public <T> void setValue(Ref<T> ref, T value) {
        if(ref instanceof WithId withId) {
            String id = withId.id();
            RefEntity refEntity = refMapper.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到id为" + id + "的ref"));
            refEntity.setValue(FormulaParser.writeLiteralToString(value, ref.type().typeName()));
            refEntity.setUpdateTime(new Date());
            refMapper.save(refEntity);
        }
    }
}

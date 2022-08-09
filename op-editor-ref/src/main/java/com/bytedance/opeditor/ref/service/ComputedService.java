package com.bytedance.opeditor.ref.service;

import com.bytedance.opeditor.ref.entity.RefEntity;
import com.bytedance.opeditor.ref.mapper.RefMapper;
import com.bytedance.opeditor.ref.refs.WithId;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import icu.fordring.ref.Ref;
import icu.fordring.ref.Watcher;
import icu.fordring.ref.impl.BasicRefContext;
import icu.fordring.ref.refs.ComputedRef;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Service
public class ComputedService extends BasicRefContext {
    @Resource
    private FormulaExecutor formulaExecutor;
    @Resource
    private RefMapper refMapper;

    @Transactional
    @Override
    public <T> void setRely(Set<Ref<?>> refs, ComputedRef<T> computedRef) {
        if(computedRef instanceof WithId withId) {
            String id = withId.id();
            RefEntity refEntity = refMapper.findById(id).orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND, "找不到id为"+id+"的计算属性"));
            Set<String> dependentIds = refs.stream().map(r -> {
                if (r instanceof WithId wi) {
                    return wi.id();
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet());
            Set<String> oldDependentIds = new HashSet<>(refEntity.getDependent());
            setRely(id, dependentIds, oldDependentIds);
            refEntity.setDependent(new ArrayList<>(dependentIds));
            refMapper.save(refEntity);
        } else {
            throw new IllegalArgumentException("传入了不支持的ref格式");
        }
    }

    private void setRely(String id, Set<String> dependentIds, Set<String> oldDependentIds) {
        Sets.difference(dependentIds, oldDependentIds).forEach(i->{
            RefEntity ref = refMapper.findById(id).orElse(null);
            if(ref==null) return;
            ArrayList<String> newAffect = new ArrayList<>(ref.getAffect().size()+1);
            newAffect.addAll(ref.getAffect());
            newAffect.add(i);
            ref.setAffect(newAffect);
            refMapper.save(ref);
        });
        Sets.difference(oldDependentIds, dependentIds).forEach(i->{
            RefEntity ref = refMapper.findById(id).orElse(null);
            if(ref==null) return;
            ArrayList<String> newAffect = new ArrayList<>(ref.getAffect().size());
            newAffect.addAll(ref.getAffect());
            newAffect.remove(i);
            ref.setAffect(newAffect);
            refMapper.save(ref);
        });
    }

    @Override
    public <T> void addWatch(Ref<T> ref, Watcher<T> watcher) {

    }

    @Override
    protected Iterable<ComputedRef<?>> getRely(Ref<?> ref) {
        return null;
    }

    @Override
    protected <T> Set<Watcher<Object>> getWatch(Ref<T> ref) {
        return null;
    }

    public Map<ComputedRef<?>, Set<ComputedRef<?>>> getAllRely(Set<String> ids) {
        return null;
    }
}

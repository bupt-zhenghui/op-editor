package com.bytedance.opeditor.ref.service;

import com.bytedance.opeditor.ref.entity.RefEntity;
import com.bytedance.opeditor.ref.mapper.RefMapper;
import com.bytedance.opeditor.ref.refs.ProComputedRef;
import com.bytedance.opeditor.ref.refs.ProRef;
import com.bytedance.opeditor.ref.refs.WithId;
import com.bytedance.opeditor.ref.service.version.VersionControl;
import com.google.common.collect.Sets;
import icu.fordring.formula.FormulaParser;
import icu.fordring.ref.*;
import icu.fordring.ref.impl.BasicRefContext;
import icu.fordring.ref.refs.ComputedRef;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class ComputedService extends BasicRefContext {
    @Resource
    private FormulaExecutor formulaExecutor;
    @Resource
    private RefMapper refMapper;
    @Resource
    private RefStore refStore;
    @Resource
    private VersionControl versionControl;

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
            Set<String> oldDependentIds = new HashSet<>(Optional.ofNullable(refEntity.getDependent()).orElse(Collections.emptyList()));
            setRely(id, dependentIds, oldDependentIds);
            refEntity.setDependent(new ArrayList<>(dependentIds));
            refMapper.save(refEntity);
        } else {
            throw new IllegalArgumentException("传入了不支持的ref格式");
        }
    }

    private void setRely(String id, Set<String> dependentIds, Set<String> oldDependentIds) {
        Sets.difference(dependentIds, oldDependentIds).forEach(i->{
            RefEntity ref = refMapper.findById(i).orElse(null);
            if(ref==null) return;
            List<String> affect = Optional.ofNullable(ref.getAffect()).orElse(Collections.emptyList());
            ArrayList<String> newAffect = new ArrayList<>(affect.size()+1);
            newAffect.addAll(affect);
            newAffect.add(id);
            ref.setAffect(newAffect);
            refMapper.save(ref);
        });
        Sets.difference(oldDependentIds, dependentIds).forEach(i->{
            RefEntity ref = refMapper.findById(i).orElse(null);
            if(ref==null) return;
            List<String> affect = Optional.ofNullable(ref.getAffect()).orElse(Collections.emptyList());
            ArrayList<String> newAffect = new ArrayList<>(affect.size());
            newAffect.addAll(affect);
            newAffect.remove(id);
            ref.setAffect(newAffect);
            refMapper.save(ref);
        });
    }

    @Override
    public <T> void addWatch(Ref<T> ref, Watcher<T> watcher) {

    }

    @Override
    protected List<ComputedRef<?>> getRely(Ref<?> ref) {
        if(ref instanceof WithId withId) {
            String id = withId.id();
            RefEntity refEntity = refMapper.findById(id).orElse(null);
            if(refEntity==null) return Collections.emptyList();
            List<String> affectIds = refEntity.getAffect();
            List<RefEntity> refs = refMapper.findAllByIdIn(affectIds);
            if (refs==null) return null;
            Map<ComputedRef<?>, Set<ComputedRef<?>>> map = new HashMap<>();
            LinkedList<ComputedRef<?>> queue = refs.stream().map(this::findComputedRef).collect(Collectors.toCollection(LinkedList::new));
            while(!queue.isEmpty()) {
                ComputedRef<?> r = queue.pollFirst();
                if(map.containsKey(r)) continue;
                if(r instanceof WithId rid) {
                    String i = rid.id();
                    Set<ComputedRef<?>> computedRefs = refMapper.findById(i)
                            .map(RefEntity::getAffect)
                            .map(refMapper::findAllByIdIn)
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(this::findComputedRef)
                            .collect(Collectors.toSet());
                    map.put(r, computedRefs);
                    queue.addAll(computedRefs);
                }
            }
            List<ComputedRef<?>> computedRefs = topologicalSort(map);
            log.info("指标更新:{}", computedRefs.stream().map(r->{
                if(r instanceof WithId) {
                    return ((WithId) r).id();
                }
                return "Error@"+Integer.toHexString(r.hashCode());
            }).collect(Collectors.toSet()));
            return computedRefs;
        }
        return Collections.emptyList();
    }


    @Override
    protected <T> Set<Watcher<Object>> getWatch(Ref<T> ref) {
        return null;
    }

    public ComputedRef<Object> findComputedRef(RefEntity refEntity) {
        if(Objects.equals(refEntity.getIsComputed(), false)) return null;
        String formulaId = refEntity.getFormulaId();
        if(formulaId==null) return null;
        Ref<Object> ref = FormulaParser.parseLiteral(refEntity.getValue(), refEntity.getType());
        return new ProComputedRef<>(refEntity.getId() ,()-> {
            long version = versionControl.incrAndGetGlobalVersion();
            Ref<?> r = formulaExecutor.executeFormula(formulaId);
            if(!Objects.equals(r.type().typeName(), ref.type().typeName())) {
                log.warn("Ref类型出现异常:\n{}\nexpect:{} actual:{}", refEntity, r.type().typeName(), ref.type().typeName());
            }
            boolean updateSuccess = versionControl.compareAndSetVersion(refEntity.getId(), version);
            if(!updateSuccess) {
                throw new SkippedUpdateException();
            }
            return r.value();
        }, ref.type(), this, refStore);
    }

    public Ref<Object> findRef(String refId) {
        RefEntity refEntity = refMapper.findById(refId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        if(refEntity.getIsComputed()) {
            return findComputedRef(refEntity);
        } else {
            return new ProRef(refId, refEntity.getType(), this, refStore);
        }
    }

    public MutRef<Object> findMutRef(String refId) {
        RefEntity refEntity = refMapper.findById(refId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        if(refEntity.getIsComputed()) {
            return null;
        } else {
            return new ProRef(refId, refEntity.getType(), this, refStore);
        }
    }
}

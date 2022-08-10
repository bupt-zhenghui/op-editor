package icu.fordring.ref.impl;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import icu.fordring.ref.Ref;
import icu.fordring.ref.RefContext;
import icu.fordring.ref.SkippedUpdateException;
import icu.fordring.ref.Watcher;
import icu.fordring.ref.refs.ComputedRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author fordring
 * @since 2022/8/6
 */
public abstract class BasicRefContext implements RefContext {
    private static class SniffContext {
        int reentrant = 1;
        Set<Ref<?>> refs = new HashSet<>();
    }
    private final ThreadLocal<SniffContext> sniffContext = new ThreadLocal<>();

    private static class UpdateContext {
        int reentrant = 1;
        Multiset<Ref<?>> refs = HashMultiset.create();
    }
    private final ThreadLocal<UpdateContext> updateContext = new ThreadLocal<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 返回与ref相通的节点图的拓扑排序
     * @return
     */
    protected abstract List<ComputedRef<?>> getRely(Ref<?> ref);

    protected abstract <T> Set<Watcher<Object>> getWatch(Ref<T> ref);

    @SuppressWarnings("unchecked")
    @Override
    public <T> void emitChange(Ref<T> ref, T oldValue) {
        startUpdate();
        UpdateContext updateContext = this.updateContext.get();
        if(updateContext.reentrant>1) {
            updateContext.refs.add(ref);
            stopUpdate();
            return;
        }
        int count = updateContext.refs.count(ref);
        if(count>0) {
            stopUpdate();
            logger.error("检查到computed<{}@{}>可能存在的环状调用，已忽略此次更新。", ref.type().typeName(), ref);
            return;
        }
        updateContext.refs.add(ref);
        try {
            Optional.ofNullable(getRely(ref)).ifPresent(rs-> rs.forEach(ComputedRef::update));
        } catch (SkippedUpdateException ignore) {}
        boolean isStopped = stopUpdate();
        if(isStopped) {
            updateContext.refs.forEach(r->{
                Optional.ofNullable(getWatch(r)).ifPresent(ws->ws.forEach(w->w.accept((Ref<Object>) ref, oldValue)));
            });
        }
    }
    private void startUpdate() {
        UpdateContext updateContext = this.updateContext.get();
        if(updateContext==null || updateContext.reentrant<=0) {
            this.updateContext.set(new UpdateContext());
        } else {
            updateContext.reentrant++;
        }
    }
    private boolean stopUpdate() {
        UpdateContext updateContext = this.updateContext.get();
        if(updateContext==null || updateContext.reentrant<=0) return true;
        updateContext.reentrant--;
        if(updateContext.reentrant<=0) {
            this.sniffContext.remove();
            return true;
        }
        return false;
    }

    @Override
    public <T> void emitGet(Ref<T> ref) {
        if(isSniffing()) sniffContext.get().refs.add(ref);
    }

    @Override
    public void startSniff() {
        SniffContext sniffContext = this.sniffContext.get();
        if(sniffContext==null || sniffContext.reentrant<=0) {
            this.sniffContext.set(new SniffContext());
        } else {
            sniffContext.reentrant++;
        }
    }

    @Override
    public boolean isSniffing() {
        return sniffContext.get()!=null;
    }

    @Override
    public Set<Ref<?>> stopSniff() {
        SniffContext sniffContext = this.sniffContext.get();
        if(sniffContext==null || sniffContext.reentrant<=0) return Collections.emptySet();
        Set<Ref<?>> refs = sniffContext.refs;
        sniffContext.reentrant--;
        if(sniffContext.reentrant<=0) this.sniffContext.remove();
        return refs;
    }

    protected List<ComputedRef<?>> topologicalSort(Map<ComputedRef<?>, Set<ComputedRef<?>>> map) {
        LinkedList<ComputedRef<?>> ans = new LinkedList<>();
        LinkedList<ComputedRef<?>> nextList = new LinkedList<>();
        Map<ComputedRef<?>, Integer> inDegreeMap = new HashMap<>();
        map.forEach((node, tos)->{
            inDegreeMap.putIfAbsent(node, 0);
            tos.forEach(r->{
                inDegreeMap.merge(r, 1, Integer::sum);
            });
        });
        inDegreeMap.entrySet().removeIf(entry->{
            boolean needMove = entry.getValue().equals(0);
            if(needMove) nextList.add(entry.getKey());
            return needMove;
        });
        while(!nextList.isEmpty()) {
            ComputedRef<?> popRef = nextList.poll();
            ans.add(popRef);
            Set<ComputedRef<?>> refs = map.get(popRef);
            if (refs==null) continue;
            refs.forEach(r->{
                if(inDegreeMap.containsKey(r)) {
                    Integer mergedCnt = inDegreeMap.merge(r, -1, Integer::sum);
                    if(mergedCnt<=0) {
                        nextList.add(r);
                        inDegreeMap.remove(r);
                    }
                }
            });
        }
        ans.addAll(inDegreeMap.keySet());
        return ans;
    }
}

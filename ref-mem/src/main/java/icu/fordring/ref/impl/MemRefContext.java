package icu.fordring.ref.impl;

import icu.fordring.ref.Ref;
import icu.fordring.ref.Watcher;
import icu.fordring.ref.refs.ComputedRef;

import java.util.*;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class MemRefContext extends BasicRefContext{
    /**
     * Map<
     *  Ref<?>,             --> 某个ref
     *  Set<ComputedRef<?>> --> 依赖于某个ref的计算属性集合
     * >
     */
    private final Map<Ref<?>, Set<ComputedRef<?>>> relyMap = new HashMap<>();
    private final Map<Ref<?>, Set<Watcher<Object>>> watchMap = new HashMap<>();

    public <T1, T2> void addRely(Ref<T1> tar, ComputedRef<T2> rely) {
        if(!relyMap.containsKey(tar)) relyMap.put(tar, new HashSet<>());
        relyMap.get(tar).add(rely);
    }

    @Override
    public <T> void setRely(Set<Ref<?>> refs, ComputedRef<T> tComputedRef) {
        refs.forEach(r->addRely(r, tComputedRef));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void addWatch(Ref<T> ref, Watcher<T> watcher) {
        if(!watchMap.containsKey(ref)) watchMap.put(ref, new HashSet<>());
        watchMap.get(ref).add((Watcher<Object>) watcher);
    }

    @Override
    protected List<ComputedRef<?>> getRely(Ref<?> ref) {
        Set<ComputedRef<?>> refs = relyMap.get(ref);
        if (refs==null) return null;
        Map<ComputedRef<?>, Set<ComputedRef<?>>> map = new HashMap<>();
        LinkedList<ComputedRef<?>> queue = new LinkedList<>(refs);
        while(!queue.isEmpty()) {
            ComputedRef<?> r = queue.pollFirst();
            if(map.containsKey(r)) continue;
            Set<ComputedRef<?>> computedRefs = Optional.ofNullable(relyMap.get(r)).orElse(Collections.emptySet());
            map.put(r, computedRefs);
            queue.addAll(computedRefs);
        }
        return topologicalSort(map);
    }

    @Override
    protected <T> Set<Watcher<Object>> getWatch(Ref<T> ref) {
        return watchMap.get(ref);
    }
}

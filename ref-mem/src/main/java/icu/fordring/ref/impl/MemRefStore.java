package icu.fordring.ref.impl;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.refs.WithId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class MemRefStore implements RefStore {
    private final Map<Integer, Object> map = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(Ref<T> ref) {
        return (T) map.get(id((ref)));
    }

    @Override
    public <T> void setValue(Ref<T> ref, T value) {
        Integer id = id(ref);
        if(id==null) return;
        map.put(id, value);
    }

    private Integer id(Ref<?> ref) {
        if(ref instanceof WithId mr) {
            return mr.id();
        }
        return null;
    }
}

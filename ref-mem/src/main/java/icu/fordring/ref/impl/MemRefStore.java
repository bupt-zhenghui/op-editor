package icu.fordring.ref.impl;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;
import icu.fordring.ref.refs.BasicMemRef;
import icu.fordring.ref.refs.WithId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class MemRefStore implements RefStore {
    private final Map<Integer, MemRef<?>> map = new ConcurrentHashMap<>();
    private final AtomicInteger iota;

    public MemRefStore(AtomicInteger iota) {
        this.iota = iota;
    }

    private class MemRef<T> extends BasicMemRef<T> {
        private final RefType<T> type;
        private T value;
        public MemRef(Integer id, T value, RefType<T> type) {
            super(id, null, MemRefStore.this);
            this.type = type;
            this.value = value;
        }

        @Override
        protected T get() {
            return value;
        }

        @Override
        public RefType<T> type() {
            return type;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue(Ref<T> ref) {
        Integer id = id(ref);
        if(id==null) return null;
        return (T) Optional.ofNullable(map.get(id)).map(MemRef::get).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setValue(Ref<T> ref, T value) {
        Integer id = id(ref);
        if(id==null) return;
        MemRef<T> memRef = (MemRef<T>)map.get(id);
        if(memRef!=null) {
            memRef.value = value;
        } else {
            memRef = new MemRef<>(id, value, ref.type());
            map.put(id, memRef);
        }
    }

    public Ref<?> getRef(Integer id) {
        return map.get(id);
    }

    public <T> Ref<T> putRef(T value, RefType<T> type) {
        int id = iota.getAndIncrement();
        MemRef<T> ref = new MemRef<>(id, value, type);
        map.put(id, ref);
        return ref;
    }

    private Integer id(Ref<?> ref) {
        if(ref instanceof WithId mr) {
            return mr.id();
        }
        return null;
    }
}

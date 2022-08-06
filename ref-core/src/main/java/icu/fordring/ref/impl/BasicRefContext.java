package icu.fordring.ref.impl;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefContext;
import icu.fordring.ref.Watcher;
import icu.fordring.ref.refs.ComputedRef;

import java.util.*;

/**
 * @author fordring
 * @since 2022/8/6
 */
public abstract class BasicRefContext implements RefContext {
    private final ThreadLocal<Set<Ref<?>>> sniffContext = new ThreadLocal<>();

    protected abstract Set<ComputedRef<?>> getRely(Ref<?> ref);

    protected abstract <T> Set<Watcher<Object>> getWatch(Ref<T> ref);

    @SuppressWarnings("unchecked")
    @Override
    public <T> void emitChange(Ref<T> ref, T oldValue) {
        Optional.ofNullable(getRely(ref)).ifPresent(rs->rs.forEach(ComputedRef::update));
        Optional.ofNullable(getWatch(ref)).ifPresent(ws->ws.forEach(w->w.accept((Ref<Object>) ref, oldValue)));
    }

    @Override
    public <T> void emitGet(Ref<T> ref) {
        if(isSniffing()) sniffContext.get().add(ref);
    }

    @Override
    public void startSniff() {
        sniffContext.set(new HashSet<>());
    }

    @Override
    public boolean isSniffing() {
        return sniffContext.get()!=null;
    }

    @Override
    public Set<Ref<?>> stopSniff() {
        Set<Ref<?>> refs = sniffContext.get();
        sniffContext.remove();
        return refs;
    }
}

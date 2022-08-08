package icu.fordring.ref;

import icu.fordring.ref.impl.MemRefContext;
import icu.fordring.ref.impl.MemRefStore;
import icu.fordring.ref.refs.ComputedMemRef;
import icu.fordring.ref.refs.ComputedRef;
import icu.fordring.ref.refs.IntMemRef;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class MemRefFactory implements RefFactory{
    private final AtomicInteger iota = new AtomicInteger(1);

    public final MemRefStore store = new MemRefStore(iota);
    public final MemRefContext context = new MemRefContext();

    @Override
    public MutRef<Integer> newRef(Integer val) {
        IntMemRef ref = new IntMemRef(nextId(), context, store);
        store.setValue(ref, val);
        return ref;
    }

    @Override
    public <T> ComputedRef<T> newComputed(Supplier<T> supplier, RefType<T> retType, boolean lazy) {
        ComputedMemRef<T> ref = new ComputedMemRef<>(nextId(), supplier, retType, context, store);
        if(!lazy) ref.update(false);
        return ref;
    }

    @Override
    public <T> void newWatch(Ref<T> ref, Watcher<T> watcher) {
        context.addWatch(ref, watcher);
    }

    public int nextId() {
        return iota.getAndIncrement();
    }
}

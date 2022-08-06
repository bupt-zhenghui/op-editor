package icu.fordring.ref.refs;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;

/**
 * @author fordring
 * @since 2022/8/5
 */
public abstract class BasicRef<T> implements Ref<T> {
    protected final RefContext context;
    protected final RefStore store;

    protected BasicRef(RefContext context, RefStore store) {
        this.context = context;
        this.store = store;
    }

    @Override
    public T value() {
        context.emitGet(this);
        return get();
    }

    protected T get() {
        return store.getValue(this);
    }
}

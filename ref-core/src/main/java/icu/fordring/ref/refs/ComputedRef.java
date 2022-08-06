package icu.fordring.ref.refs;


import icu.fordring.ref.Ref;
import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/5
 */
public class ComputedRef<T> extends BasicRef<T> {
    private final Supplier<T> supplier;
    private final RefType<T> type;

    public ComputedRef(Supplier<T> supplier, RefType<T> type, RefContext context, RefStore store) {
        super(context, store);
        this.supplier = supplier;
        this.type = type;
    }

    @Override
    public RefType<T> type() {
        return type;
    }

    public void update() {
        update(true);
    }
    public void update(boolean emit) {
        context.startSniff();
        T t = supplier.get();
        Set<Ref<?>> refs = context.stopSniff();
        context.addRely(refs, this);
        T oldVal = value();
        store.setValue(this, t);
        if(!Objects.equals(t, oldVal) && emit) {
            context.emitChange(this, oldVal);
        }
    }
}

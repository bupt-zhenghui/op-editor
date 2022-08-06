package icu.fordring.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;

import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class ComputedMemRef<T> extends ComputedRef<T> implements WithId{
    protected Integer id;

    public ComputedMemRef(Integer id, Supplier<T> supplier, RefType<T> type, RefContext context, RefStore store) {
        super(supplier, type, context, store);
        this.id = id;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComputedMemRef<?> that = (ComputedMemRef<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id;
    }
}

package com.bytedance.opeditor.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;
import icu.fordring.ref.refs.ComputedRef;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/9
 */
public class ProComputedRef<T> extends ComputedRef<T> implements WithId {
    protected String id;
    public ProComputedRef(String id, Supplier<T> supplier, RefType<T> type, RefContext context, RefStore store) {
        super(supplier, type, context, store);
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProComputedRef<?> that = (ProComputedRef<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package icu.fordring.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;

/**
 * @author fordring
 * @since 2022/8/6
 */
public abstract class BasicMemRef<T> extends BasicMutRef<T> implements WithId {
    protected Integer id;

    public BasicMemRef(Integer id, RefContext context, RefStore store) {
        super(context, store);
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
        BasicMemRef<?> that = (BasicMemRef<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id;
    }
}

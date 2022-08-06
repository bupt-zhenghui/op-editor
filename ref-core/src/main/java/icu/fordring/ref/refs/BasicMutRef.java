package icu.fordring.ref.refs;


import icu.fordring.ref.MutRef;
import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;

import java.util.Objects;

/**
 * @author fordring
 * @since 2022/8/5
 */
public abstract class BasicMutRef<T> extends BasicRef<T> implements MutRef<T> {

    public BasicMutRef(RefContext context, RefStore store) {
        super(context, store);
    }

    @Override
    public void set(T val) {
        T oldVal = this.value();
        store.setValue(this, val);
        if(!Objects.equals(oldVal,val)) context.emitChange(this, oldVal);
    }
}

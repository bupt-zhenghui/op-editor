package com.bytedance.opeditor.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.refs.BasicMutRef;

/**
 * @author fordring
 * @since 2022/8/6
 */
public abstract class BasicProRef<T> extends BasicMutRef<T> implements WithId {
    protected String id;

    public BasicProRef(String id, RefContext context, RefStore store) {
        super(context, store);
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }
}

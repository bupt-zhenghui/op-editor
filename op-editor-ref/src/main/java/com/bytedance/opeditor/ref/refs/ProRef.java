package com.bytedance.opeditor.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;
import icu.fordring.ref.refs.BasicMutRef;

import java.util.Objects;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class ProRef extends BasicMutRef<Object> implements WithId {
    protected String id;
    protected String type;

    public ProRef(String id, String type, RefContext context, RefStore store) {
        super(context, store);
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
        ProRef that = (ProRef) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public RefType<Object> type() {
        return new RefType<Object>() {
            @Override
            public String typeName() {
                return type;
            }

            @Override
            public Class<Object> typeClass() {
                return Object.class;
            }
        };
    }
}

package com.bytedance.opeditor.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;
import icu.fordring.ref.Types;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class IntProRef extends BasicProRef<Integer> {
    public IntProRef(String id, RefContext context, RefStore store) {
        super(id, context, store);
    }

    @Override
    public RefType<Integer> type() {
        return Types.INT_TYPE;
    }
}

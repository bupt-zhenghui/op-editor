package icu.fordring.ref.refs;

import icu.fordring.ref.RefContext;
import icu.fordring.ref.RefStore;
import icu.fordring.ref.RefType;
import icu.fordring.ref.Types;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class IntMemRef extends BasicMemRef<Integer>{
    public IntMemRef(Integer id, RefContext context, RefStore store) {
        super(id, context, store);
    }

    @Override
    public RefType<Integer> type() {
        return Types.INT_TYPE;
    }
}

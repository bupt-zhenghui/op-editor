package icu.fordring.ref.refs;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefType;
import icu.fordring.ref.Types;

/**
 * @author fordring
 * @since 2022/8/8
 */
public record IntRef(Integer value) implements Ref<Integer> {

    @Override
    public RefType<Integer> type() {
        return Types.INT_TYPE;
    }
}

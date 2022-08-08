package icu.fordring.ref.types;

import icu.fordring.ref.RefType;

/**
 * @author fordring
 * @since 2022/8/5
 */
public class IntType implements RefType<Integer> {
    @Override
    public String typeName() {
        return "int";
    }

    @Override
    public Class<Integer> typeClass() {
        return Integer.class;
    }
}

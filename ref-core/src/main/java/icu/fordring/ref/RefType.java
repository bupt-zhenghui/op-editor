package icu.fordring.ref;

/**
 * @author fordring
 * @since 2022/8/5
 */
public interface RefType<T> {
    default String typeName() {
        return this.typeClass().getSimpleName();
    }

    Class<T> typeClass();
}

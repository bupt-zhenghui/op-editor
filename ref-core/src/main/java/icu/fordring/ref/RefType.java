package icu.fordring.ref;

/**
 * @author fordring
 * @since 2022/8/5
 */
public interface RefType<T> {
    String typeName();

    Class<T> typeClass();
}

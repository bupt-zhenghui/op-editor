package icu.fordring.ref;

/**
 * @author fordring
 * @since 2022/8/6
 */
public interface RefStore {
    <T> T getValue(Ref<T> ref);
    <T> void setValue(Ref<T> ref, T value);
}

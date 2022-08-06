package icu.fordring.ref;

/**
 * @author fordring
 * @since 2022/8/5
 */
public interface Ref<T> {
    RefType<T> type();
    T value();

    default boolean isEmpty() {
        return this.value()==null;
    }
}

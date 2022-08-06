package icu.fordring.ref;

/**
 * @author fordring
 * @since 2022/8/5
 */
public interface MutRef<T> extends Ref<T> {
    void set(T val);
}

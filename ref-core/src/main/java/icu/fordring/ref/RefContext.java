package icu.fordring.ref;

import icu.fordring.ref.refs.ComputedRef;

import java.util.Set;

/**
 * @author fordring
 * @since 2022/8/6
 */
public interface RefContext {
    <T> void emitChange(Ref<T> ref, T oldValue);
    <T> void emitGet(Ref<T> ref);

    boolean isSniffing();
    void startSniff();
    Set<Ref<?>> stopSniff();

    <T> void setRely(Set<Ref<?>> refs, ComputedRef<T> tComputedRef);

    <T> void addWatch(Ref<T> ref, Watcher<T> watcher);
}

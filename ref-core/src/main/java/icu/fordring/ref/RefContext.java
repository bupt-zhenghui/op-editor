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

    <T1,T2> void addRely(Ref<T1> tar, ComputedRef<T2> rely);

    default <T> void addRely(Set<Ref<?>> refs, ComputedRef<T> tComputedRef) {
        refs.forEach(r->addRely(r, tComputedRef));
    }

    <T> void addWatch(Ref<T> ref, Watcher<T> watcher);
}

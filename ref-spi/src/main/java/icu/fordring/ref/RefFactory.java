package icu.fordring.ref;

import icu.fordring.ref.refs.ComputedRef;

import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/6
 */
public interface RefFactory {
    MutRef<Integer> newRef(Integer val);

    <T> ComputedRef<T> newComputed(Supplier<T> supplier, RefType<T> retType,boolean lazy);
    <T> void newWatch(Ref<T> ref, Watcher<T> watcher);
}

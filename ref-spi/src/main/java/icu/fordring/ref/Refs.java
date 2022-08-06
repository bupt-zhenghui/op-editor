package icu.fordring.ref;

import icu.fordring.ref.refs.ComputedRef;

import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class Refs {
    public static final RefFactory refFactory = ServiceLoader.load(RefFactory.class).findFirst().orElse(null);
    public static RefFactory refFactory() {
        return refFactory;
    }

    public static MutRef<Integer> ref(Integer val) {
        return refFactory.newRef(val);
    }

    public static <T> ComputedRef<T> computed(Supplier<T> supplier, RefType<T> retType) {
        return refFactory.newComputed(supplier, retType);
    }

    public static <T> void watch(Ref<T> ref, Watcher<T> watcher) {
        refFactory.newWatch(ref, watcher);
    }
}

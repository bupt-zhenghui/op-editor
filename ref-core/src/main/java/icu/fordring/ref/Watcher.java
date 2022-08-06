package icu.fordring.ref;

import java.util.function.BiConsumer;

/**
 * @author fordring
 * @since 2022/8/6
 */
@FunctionalInterface
public interface Watcher<T> extends BiConsumer<Ref<T>, T> {
}

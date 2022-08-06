package icu.fordring.ref;

import icu.fordring.ref.refs.ComputedRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fordring
 * @since 2022/8/6
 */
class RefsTest {
    private final Logger logger = LoggerFactory.getLogger(RefsTest.class);

    @Test
    public void computed() {
        MutRef<Integer> ref1 = Refs.ref(1);
        MutRef<Integer> ref2 = Refs.ref(2);
        ComputedRef<Integer> computed1 = Refs.computed(() -> ref1.value()+1, Types.INT_TYPE);

        Assertions.assertEquals(2, computed1.value());
        ref1.set(2);
        Assertions.assertEquals(3, computed1.value());

        ComputedRef<Integer> computed2 = Refs.computed(() -> ref2.value()+computed1.value()-1, Types.INT_TYPE);

        Assertions.assertEquals(4, computed2.value());
        ref1.set(12);
        Assertions.assertEquals(14, computed2.value());
    }

    @Test
    void watch() {
        MutRef<Integer> ref1 = Refs.ref(1);
        AtomicInteger i1 = new AtomicInteger(0);
        Refs.watch(ref1, (r, oldVal)->{
            logger.info("ref1更新~ new:{},old:{}", r.value(), oldVal);
            i1.incrementAndGet();
        });
        ref1.set(1);
        Assertions.assertEquals(0, i1.get());
        ref1.set(2);
        Assertions.assertEquals(1, i1.get());

        ComputedRef<Integer> computed1 = Refs.computed(() -> ref1.value()+1, Types.INT_TYPE);
        AtomicInteger i2 = new AtomicInteger(0);
        Refs.watch(computed1, (r, oldVal)->{
            logger.info("computed1更新~ new:{},old:{}", r.value(), oldVal);
            i2.incrementAndGet();
        });
        ref1.set(2);
        Assertions.assertEquals(0, i2.get());
        ref1.set(3);
        Assertions.assertEquals(1, i2.get());
        Assertions.assertEquals(2, i1.get());
    }
}

package icu.fordring.ref;

import icu.fordring.ref.refs.ComputedRef;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author fordring
 * @since 2022/8/6
 */
public class RefsTest {
    private final Logger logger = LoggerFactory.getLogger(RefsTest.class);

    @Test
    public void computed() {
        MutRef<Integer> ref1 = Refs.ref(1);
        MutRef<Integer> ref2 = Refs.ref(2);
        ComputedRef<Integer> computed1 = Refs.computed(
                () -> {
                    Integer value = ref1.value();
                    return value+1;
                },
                Types.INT_TYPE);

        ComputedRef<Integer> computed2 = Refs.computed(() -> ref2.value()+computed1.value()-1, Types.INT_TYPE);

        assertEquals(2, computed1.value());
        ref1.set(2);
        assertEquals(3, computed1.value());


        assertEquals(4, computed2.value());
        ref1.set(12);
        assertEquals(14, computed2.value());
    }

    @Test
    public void testComputed1() {
        MutRef<Integer> ref1 = Refs.ref(0);
        ComputedRef<Integer> computed1 = Refs.computed(ref1::value, Types.INT_TYPE);
        ComputedRef<Integer> computed2 = Refs.computed(() -> ref1.value()+computed1.value(), Types.INT_TYPE);
        ComputedRef<Integer> computed3 = Refs.computed(() -> ref1.value()+computed1.value()+computed2.value(), Types.INT_TYPE);
        ComputedRef<Integer> computed4 = Refs.computed(() -> ref1.value()+computed1.value()+computed2.value()+computed3.value(), Types.INT_TYPE);

        assertEquals(0, computed1.value());
        assertEquals(0, computed2.value());
        assertEquals(0, computed3.value());
        assertEquals(0, computed4.value());

        ref1.set(1);
        assertEquals(1, computed1.value());
        assertEquals(2, computed2.value());
        assertEquals(4, computed3.value());
        assertEquals(8, computed4.value());
    }

    @Disabled
    @Test
    public void testComputed2() {
        AtomicReference<ComputedRef<Integer>> computed1 = new AtomicReference<>();
        AtomicReference<ComputedRef<Integer>> computed2 = new AtomicReference<>();
        computed1.set(Refs.computed(()->{
            Integer value = computed2.get().value();
            if(value==null) return 0;
            return value+1;
        }, Types.INT_TYPE, true));
        computed2.set(Refs.computed(()->{
            Integer value = computed1.get().value();
            if(value==null) return 0;
            return value+1;
        }, Types.INT_TYPE, true));

        computed1.get().update(false);
        assertEquals(0, computed1.get().value());

        computed2.get().update();
        assertEquals(2, computed1.get().value());
        assertEquals(2, computed1.get().value());
    }

    @Test
    public void watch() {
        MutRef<Integer> ref1 = Refs.ref(1);
        AtomicInteger i1 = new AtomicInteger(0);
        Refs.watch(ref1, (r, oldVal)->{
            logger.info("ref1更新~ new:{},old:{}", r.value(), oldVal);
            i1.incrementAndGet();
        });
        ref1.set(1);
        assertEquals(0, i1.get());
        ref1.set(2);
        assertEquals(1, i1.get());

        ComputedRef<Integer> computed1 = Refs.computed(() -> ref1.value()+1, Types.INT_TYPE);
        AtomicInteger i2 = new AtomicInteger(0);
        Refs.watch(computed1, (r, oldVal)->{
            logger.info("computed1更新~ new:{},old:{}", r.value(), oldVal);
            i2.incrementAndGet();
        });
        ref1.set(2);
        assertEquals(0, i2.get());
        ref1.set(3);
        assertEquals(1, i2.get());
        assertEquals(2, i1.get());
    }
}

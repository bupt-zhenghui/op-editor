package com.bytedance.opeditor.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2021/11/9
 */
@Slf4j
public final class BeanMapper {
    private static final boolean USE_CGLIB = "cglib".equals(System.getProperty("bean-mapper"));
    private static final ConcurrentHashMap<CKey, BeanCopier> copierCache
            = new ConcurrentHashMap<>();
    private static final class CKey {
        private final Class<?> s,t;
        public CKey(Class<?> s, Class<?> t) {
            this.s = s;
            this.t = t;
        }
        @Override
        public boolean equals(Object o) {
            if(o instanceof CKey o1) {
                return o1.s==s&&o1.t==t;
            }
            return false;
        }
        @Override
        public int hashCode() {
            return Objects.hash(s, t);
        }
    }
    public static int cglibCacheSize() {
        return copierCache.size();
    }

    public static <S, T> void cglibMap(S s, T t) {
        if(s==null||t==null) return;
        CKey cKey = new CKey(s.getClass(), t.getClass());
        BeanCopier beanCopier = copierCache.get(cKey);
        if(beanCopier==null) {
            beanCopier = BeanCopier.create(s.getClass(), t.getClass(), false);
            copierCache.put(cKey, beanCopier);
            log.info("已创建bean映射:[{}]->[{}]", s.getClass().getSimpleName(), t.getClass().getSimpleName());
        }
        beanCopier.copy(s, t, null);
    }

    public static <S, T> void springMap(S s, T t) {
        BeanUtils.copyProperties(s, t);
    }

    public static <S, T> void map(S s, T t) {
        if(USE_CGLIB) cglibMap(s,t);
        else springMap(s,t);
    }

    public static <S,T> T map(S s, Supplier<T> tSupplier) {
        if(s==null) return null;
        T t = tSupplier.get();
        map(s, t);
        return t;
    }

    public static <S,T> Function<S,T> mapFunc(Supplier<T> tSupplier) {
        return s->map(s, tSupplier);
    }
}

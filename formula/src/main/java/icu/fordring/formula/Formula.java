package icu.fordring.formula;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefType;

import java.util.Collections;
import java.util.List;

/**
 * @author fordring
 * @since 2022/8/8
 */
public interface Formula<T> {
    /**
     * @return 参数类型列表
     */
    default List<RefType<?>> args() {
        return Collections.emptyList();
    }

    /**
     * @return 是否可变长参数
     */
    default boolean variableLengthArgs() {
        return false;
    }

    /**
     * @param args 为这个表达式示例设置参数 (设置的是{@code Formula}对象，意味着参数还没有被计算出来)
     */
    default void setArgs(List<Formula<?>> args) {}

    /**
     * @param ref 当函数为字面量时被调用，直接传入ref。
     */
    default void setRef(Ref<T> ref) {}

    /**
     * 执行这个函数。
     * 函数可以自己决定如何聚合参数。
     * 函数可以自己决定是否计算哪些参数。
     *
     * @return 计算结果
     */
    Ref<T> exec();

    // 元信息
    FormulaMeta meta();
}

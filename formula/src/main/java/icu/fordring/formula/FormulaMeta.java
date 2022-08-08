package icu.fordring.formula;

import java.util.function.Supplier;

/**
 * @author fordring
 * @since 2022/8/8
 */
public interface FormulaMeta {
    /**
     * @return 函数的名字，全局唯一
     */
    String name();

    /**
     * @return 构造一个新的函数实例
     */
    Formula<?> newInstance();


    record ClassicFormulaMeta(String name,
                              Supplier<Formula<?>> constructor) implements FormulaMeta {
        @Override
        public Formula<?> newInstance() {
            return constructor.get();
        }
    }

    static FormulaMeta of(String name, Supplier<Formula<?>> formulaSupplier) {
        return new ClassicFormulaMeta(name, formulaSupplier);
    }
}

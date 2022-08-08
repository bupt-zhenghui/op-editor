package icu.fordring.formula.operator;

import icu.fordring.formula.BasicFormula;
import icu.fordring.formula.FormulaMeta;
import icu.fordring.ref.Ref;
import icu.fordring.ref.RefType;
import icu.fordring.ref.Types;
import icu.fordring.ref.refs.IntRef;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class IntAddFormula extends BasicFormula<Integer> {
    @Override
    public List<RefType<?>> args() {
        // 函数的参数列表为(int)
        return Collections.singletonList(Types.INT_TYPE);
    }

    @Override
    public boolean variableLengthArgs() {
        // 表示该函数启用了动态参数列表，因此函数实际上的参数列表为(int,...int)
        return true;
    }

    @Override
    protected Ref<Integer> doExec(List<Ref<?>> args) {
        // BasicFormula实现了exec()，会自动算出所有参数
        return new IntRef(reduce(args.stream().mapToInt(r->(Integer) r.value())));
    }

    @Override
    public FormulaMeta meta() {
        return FormulaMeta.of("add-int", IntAddFormula::new);
    }

    protected Integer reduce(IntStream intStream) {
        return intStream.sum();
    }
}

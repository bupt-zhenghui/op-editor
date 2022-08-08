package icu.fordring.formula.operator;

import icu.fordring.formula.BasicFormula;
import icu.fordring.formula.FormulaMeta;
import icu.fordring.ref.Ref;
import icu.fordring.ref.RefType;
import icu.fordring.ref.Types;
import icu.fordring.ref.refs.IntRef;

import java.util.Collections;
import java.util.List;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class IntMultiFormula extends BasicFormula<Integer> {
    @Override
    public List<RefType<?>> args() {
        return Collections.singletonList(Types.INT_TYPE);
    }

    @Override
    public boolean variableLengthArgs() {
        return true;
    }

    @Override
    protected Ref<Integer> doExec(List<Ref<?>> args) {
        return new IntRef(args.stream().mapToInt(value -> (Integer)value.value()).reduce((a,b)->a*b).orElse(0));
    }

    @Override
    public FormulaMeta meta() {
        return FormulaMeta.of("multi-int", IntMultiFormula::new);
    }
}

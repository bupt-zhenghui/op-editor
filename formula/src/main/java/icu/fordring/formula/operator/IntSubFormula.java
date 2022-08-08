package icu.fordring.formula.operator;

import icu.fordring.formula.Formula;
import icu.fordring.formula.FormulaMeta;

import java.util.stream.IntStream;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class IntSubFormula extends IntAddFormula{
    @Override
    protected Integer reduce(IntStream intStream) {
        return intStream.reduce((a,b)->a-b).orElse(0);
    }

    @Override
    public FormulaMeta meta() {
        return FormulaMeta.of("sub-int", IntSubFormula::new);
    }
}

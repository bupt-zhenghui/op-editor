package icu.fordring.formula.basic;

import icu.fordring.formula.Formula;
import icu.fordring.formula.FormulaMeta;
import icu.fordring.ref.Ref;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class RefFormula<T> implements Formula<T> {
    private Ref<T> ref;

    @Override
    public void setRef(Ref<T> ref) {
        this.ref = ref;
    }

    @Override
    public Ref<T> exec() {
        return ref;
    }

    @Override
    public FormulaMeta meta() {
        return FormulaMeta.of("ref", RefFormula::new);
    }
}

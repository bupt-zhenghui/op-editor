package icu.fordring.formula;

import icu.fordring.ref.Ref;
import icu.fordring.ref.RefType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fordring
 * @since 2022/8/8
 */
public abstract class BasicFormula<T> implements Formula<T>{
    protected List<Formula<?>> formulaArgs;

    @Override
    public void setArgs(List<Formula<?>> args) {
        verifyArgsNum(args);
        doSetArgs(args);
    }

    protected void doSetArgs(List<Formula<?>> formulaArgs) {
        this.formulaArgs = formulaArgs;
    }

    protected void verifyArgsNum(List<Formula<?>> args) {
        List<RefType<?>> argTypes = args();
        int argLen = Optional.ofNullable(args).map(List::size).orElse(0);
        int argTypeLen = Optional.ofNullable(argTypes).map(List::size).orElse(0);
        if(argLen==0 && argTypeLen==0) return;
        assert argTypes!=null;
        boolean variableLengthArgs = variableLengthArgs();
        if(variableLengthArgs) {
            assert argLen > 0 && argLen >= argTypeLen;
        } else {
            assert argLen == argTypeLen;
        }
    }

    @Override
    public Ref<T> exec() {
        List<Ref<?>> args;
        if(formulaArgs==null || formulaArgs.isEmpty()) args = Collections.emptyList();
        else args = formulaArgs.stream().map(Formula::exec).collect(Collectors.toList());
        return doExec(args);
    }

    protected abstract Ref<T> doExec(List<Ref<?>> args);
}

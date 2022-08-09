package icu.fordring.formula;

import com.google.common.base.Strings;
import icu.fordring.formula.basic.LiteralParser;
import icu.fordring.ref.Ref;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class FormulaParser {
    private static final Map<String, FormulaMeta> FORMULA_MAP = new HashMap<>();
    private static final Map<String, LiteralParser> PARSER_MAP = new HashMap<>();

    static {
        ServiceLoader.load(Formula.class)
                .stream()
                .forEach(f->{
                    FormulaMeta meta = f.get().meta();
                    FORMULA_MAP.put(meta.name(), meta);
                });

        ServiceLoader.load(LiteralParser.class)
                .stream()
                .forEach(p->{
                    LiteralParser literalParser = p.get();
                    List<String> types = literalParser.supportTypes();
                    types.forEach(t-> PARSER_MAP.put(t, literalParser));
                });
    }

    @SuppressWarnings("unchecked")
    public static <T> Formula<T> parse(FormulaObject formulaObject, Function<Object, Ref<T>> getRef) {
        String operatorName = formulaObject.getOperatorName();
        FormulaMeta formulaMeta = FORMULA_MAP.get(operatorName);
        if(formulaMeta==null) throw new IllegalArgumentException("找不到名为["+operatorName+"]的算式");
        Formula<T> formula = (Formula<T>) formulaMeta.newInstance();
        String literalType = formulaObject.getLiteralType();
        Object refId = formulaObject.getRefId();
        List<FormulaObject> args = formulaObject.getArgs();
        if(!Strings.isNullOrEmpty(literalType)) {
            formula.setRef(
                    parseLiteral(formulaObject.getLiteral(), literalType)
            );
        } else if(refId !=null) {
            Ref<T> ref = getRef.apply(refId);
            if(ref==null) throw new IllegalArgumentException("找不到ref["+refId+"]");
            formula.setRef(ref);
        } else {
            List<Formula<?>> argList = args.stream().map(fo -> parse(fo, getRef)).collect(Collectors.toList());
            formula.setArgs(argList);
        }
        return formula;
    }

    @SuppressWarnings("unchecked")
    public static <T> Ref<T> parseLiteral(String value, String type) {
        LiteralParser parser = PARSER_MAP.get(type);
        if(parser==null) throw new UnsupportedOperationException("找不到支持["+type+"]类型字面量的解析器");
        return (Ref<T>) parser.parse(value, type);
    }
}

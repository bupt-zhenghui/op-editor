package icu.fordring.formula.basic.parser;

import icu.fordring.formula.basic.LiteralParser;
import icu.fordring.ref.Ref;
import icu.fordring.ref.Types;
import icu.fordring.ref.refs.IntRef;

import java.util.List;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class IntParser implements LiteralParser {
    @Override
    public Ref<?> parse(String value, String type) {
        return new IntRef(value==null? null:Integer.valueOf(value));
    }

    @Override
    public List<String> supportTypes() {
        return List.of(Types.INT_TYPE.typeName());
    }
}

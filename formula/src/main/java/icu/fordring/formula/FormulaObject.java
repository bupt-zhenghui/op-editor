package icu.fordring.formula;

import lombok.Data;

import java.util.List;

/**
 * @author fordring
 * @since 2022/8/8
 */
@Data
public class FormulaObject {
    private String operatorName;
    private List<FormulaObject> args;
    private Object refId;

    private String literal;
    private String literalType;
}

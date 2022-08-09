package com.bytedance.opeditor.ref.service;

import com.bytedance.opeditor.domain.LiteralData;
import org.springframework.stereotype.Component;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Component
public class FormulaExecutor {
    public LiteralData executeFormula(String formulaId) {
        return new LiteralData("0","int");
    }
}

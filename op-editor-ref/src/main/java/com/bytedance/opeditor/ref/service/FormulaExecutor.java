package com.bytedance.opeditor.ref.service;

import com.bytedance.opeditor.domain.LiteralData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.fordring.formula.Formula;
import icu.fordring.formula.FormulaObject;
import icu.fordring.formula.FormulaParser;
import icu.fordring.ref.Ref;
import icu.fordring.ref.refs.IntRef;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;

/**
 * @author fordring
 * @since 2022/8/9
 */
@Component
public class FormulaExecutor {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Resource @Lazy
    private ComputedService computedService;

    public FormulaExecutor() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Ref<?> executeFormula(String formulaId) {
        URL resource = ClassLoader.getSystemResource(formulaId);
        FormulaObject formulaObject;
        try {
            formulaObject = objectMapper.readValue(resource, FormulaObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Formula<Object> formula = FormulaParser.parse(formulaObject, id -> {
            if (id instanceof String idStr) {
                return computedService.findRef(idStr);
            }
            throw new UnsupportedOperationException();
        });
        return formula.exec();
    }
}

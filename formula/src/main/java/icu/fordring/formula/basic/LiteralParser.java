package icu.fordring.formula.basic;

import icu.fordring.ref.Ref;

import java.util.List;

/**
 * @author fordring
 * @since 2022/8/8
 */
public interface LiteralParser {
    /**
     *
     * @param value 字面量值
     * @param type  字面量类型
     * @return  解析完成的字面量（被Ref包装）
     */
    Ref<?> parse(String value, String type);

    /**
     *
     * @return 支持的类型列表
     */
    List<String> supportTypes();
}

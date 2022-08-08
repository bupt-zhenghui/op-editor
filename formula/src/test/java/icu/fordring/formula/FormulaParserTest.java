package icu.fordring.formula;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.fordring.ref.Ref;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author fordring
 * @since 2022/8/8
 */
public class FormulaParserTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FormulaParserTest() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Stream<Arguments> parseTests() {
        return Stream.of(
                arguments("res_2.json", 2),
                arguments("res_6.json", 6),
                arguments("res_10.json", 10),
                arguments("res_-4.json", -4),
                arguments("res_30.json", 30)
        );
    }

    @ParameterizedTest
    @MethodSource("icu.fordring.formula.FormulaParserTest#parseTests")
    public void testParse(String file, Object expect) throws IOException {
        URL resource = ClassLoader.getSystemResource(file);
        FormulaObject formulaObject = objectMapper.readValue(resource, FormulaObject.class);
        Formula<Object> formula = FormulaParser.parse(formulaObject, never -> Assertions.fail());
        Ref<Object> ans = formula.exec();
        assertEquals(expect, ans.value());
    }
}

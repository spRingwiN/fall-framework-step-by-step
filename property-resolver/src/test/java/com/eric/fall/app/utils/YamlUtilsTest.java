package com.eric.fall.app.utils;

import com.eric.property.utils.YamlUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class YamlUtilsTest {

    @Test
    public void testLoadYml() {
        Map<String, Object> configs = YamlUtils.loadYamlAsPlainMap("/application.yml");
        for (String key : configs.keySet()) {
            Object value = configs.get(key);
            System.out.println(key + ": " + value + "(" + value.getClass().getName() + ")");
        }
        assertEquals(configs.get("app.title"), "Fall Framework");
        assertEquals(configs.get("app.version"), "1.0.0");
        assertNull(configs.get("app.author"));

        assertEquals("${AUTO_COMMIT:false}", configs.get("fall.datasource.auto-commit"));
        assertEquals("level-4", configs.get("other.deep.deep.level"));

        assertEquals("0x1a2b3c", configs.get("other.hex-data"));
        assertEquals("0x1a2b3c", configs.get("other.hex-string"));


    }












}

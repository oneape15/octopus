package com.oneape.octopus.parse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-03-06 16:13.
 * Modify:
 */
public class MyFirstJUnitJupiterTests {

    @Test
    @DisplayName("1 + 1 = 2")
    void addsTwoNumbers() {
        assertEquals(2, 2, "1 + 1 should equal 2");
    }
}

package com.w3w.rm.geo.app.util;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationUtilTest {

    @Test
    public void test3WaRegexPatternValidation() {
        Assert.assertTrue(ApplicationUtil.validate3WaString("filled.count.soap"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("fdf.dfsd2321"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("fdf.dfsd.2321"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some words with space"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some words space"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some-words-without-space"));
        Assert.assertFalse(ApplicationUtil.validate3WaString("some.words.without.space"));
    }
}
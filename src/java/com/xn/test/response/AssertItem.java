package com.xn.test.response;/**
 * Created by xn056839 on 2016/9/7.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssertItem {
    private String assertKey;
    private String expect;
    private String exact;

    public AssertItem(String assertKey, String expect, String exact) {
        this.assertKey = assertKey;
        this.expect = expect;
        this.exact = exact;
    }

    public String getAssertKey() {
        return assertKey;
    }

    public String getExpect() {
        return expect;
    }

    public String getExact() {
        return exact;
    }
}

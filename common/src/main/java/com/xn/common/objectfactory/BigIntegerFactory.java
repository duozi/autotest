package com.xn.common.objectfactory;


import com.xn.common.util.StringUtil;

import java.lang.reflect.Type;
import java.math.BigInteger;


public class BigIntegerFactory extends InstanceFactory {
    @Override
    protected Object create(Type type, Object value) {
        if (StringUtil.isEmpty(value)) return null;
        return new BigInteger(value.toString());
    }

    @Override
    protected boolean support(Type type) {
        return type.equals(BigInteger.class);
    }
}

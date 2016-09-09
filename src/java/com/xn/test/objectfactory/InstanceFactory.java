package com.xn.test.objectfactory;

import java.lang.reflect.Type;

public abstract class InstanceFactory {
    protected abstract Object create(Type type, Object value);

    protected abstract boolean support(Type type);
}

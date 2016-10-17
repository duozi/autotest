package com.xn.test.objectfactory;



import com.xn.test.model.KeyValueStore;
import com.xn.test.util.ReflectionUtils;
import com.xn.test.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BeanUtils {
    private final static List<InstanceFactory> REGISTEDFACTORIES = new ArrayList<InstanceFactory>();

    private final static ObjectFactory DEFAULTFACTORY = new ObjectFactory();

    static {
        REGISTEDFACTORIES.add(new IntFactory());
        REGISTEDFACTORIES.add(new LongFactory());
        REGISTEDFACTORIES.add(new DoubleFactory());
        REGISTEDFACTORIES.add(new CharFactory());
        REGISTEDFACTORIES.add(new StringFactory());
        REGISTEDFACTORIES.add(new FloatFactory());
        REGISTEDFACTORIES.add(new ArrayFactory());
        REGISTEDFACTORIES.add(new BooleanFactory());
        REGISTEDFACTORIES.add(new MapFactory());
        REGISTEDFACTORIES.add(new DateTimeFactory());
        REGISTEDFACTORIES.add(new BigIntegerFactory());
        REGISTEDFACTORIES.add(new BigDecimalFactory());
        REGISTEDFACTORIES.add(new EnumFactory());
        REGISTEDFACTORIES.add(new ListFactory());
        REGISTEDFACTORIES.add(new StreamFactory());
    }

    /*
   按顺序匹配参数
    */
    public static Object[] getParameters(List<KeyValueStore> params, Type[] parameterTypes) {
        Object[] result = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {

                try {
                    Class<?> clazz = Class.forName(parameterTypes[i].toString().split(" ")[1]);
                    Object obj = clazz.newInstance();
                    for(KeyValueStore keyValueStore:params){
                        ReflectionUtils.setFieldValue(obj,keyValueStore.getName(),keyValueStore.getValue());
                    }

                    result[i] = create(parameterTypes[i], obj);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

//            KeyValueStore keyValuePair = params.get(i);
//            result[i] = create(parameterTypes[i], keyValuePair.getValue());
        }

        return result;
    }

    public static <T> T create(Type type, Object value) {
        for (InstanceFactory factory : REGISTEDFACTORIES) {
            if (factory.support(type)) {
                return (T) factory.create(type, value);
            }
        }
        return (T) DEFAULTFACTORY.create(type, value);
    }
}

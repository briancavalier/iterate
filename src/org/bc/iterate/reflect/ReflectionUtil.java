/*
 * Copyright (c) 2007-2010 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bc.iterate.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for common Java reflections operations, such as finding methods, dealing with boxed primitives, etc.
 *
 * @author Brian Cavalier
 */
public class ReflectionUtil
{
    private static Map<MethodCacheKey, Method> methodCache = new HashMap<MethodCacheKey, Method>(10);

    private final static Map<Class, Class> boxedTypeMapping;

    static {
        boxedTypeMapping = new HashMap<Class, Class>(7);
        boxedTypeMapping.put(Byte.class, byte.class);
        boxedTypeMapping.put(Character.class, char.class);
        boxedTypeMapping.put(Short.class, short.class);
        boxedTypeMapping.put(Integer.class, int.class);
        boxedTypeMapping.put(Long.class, long.class);
        boxedTypeMapping.put(Float.class, float.class);
        boxedTypeMapping.put(Double.class, double.class);
    }

    /**
     * Finds the named method that accepts the supplied actual parameters.
     *
     * @return the found method, if it exists.
     *
     * @throws NoSuchMethodException if a method with the supplied {@code name} that accepts the supplied {@code
     *                               actualParams}.
     */
    public static Method getMethod(Class clazz, String name, Object... actualParams) throws NoSuchMethodException
    {
        Class[] paramTypes = new Class[actualParams.length];
        for (int i = 0; i < actualParams.length; i++) {
            paramTypes[i] = getUnboxedClass(actualParams[i]);
        }

        return getMethod(clazz, name, paramTypes);
    }

    /**
     * @param boxedPrimitive boxed primitive
     *
     * @return the primitive {@link Class} (e.g. int.class) representing the type of the supplied boxed primitive (e.g.
     *         {@link Integer})
     */
    public static Class getUnboxedClass(Object boxedPrimitive)
    {
        final Class<?> c = boxedPrimitive.getClass();
        final Class unboxed = getUnboxedClass(c);
        return unboxed == null ? c : unboxed;
    }

    /**
     * @param boxedClass boxed class (e.g. {@link Integer})
     *
     * @return the primitive {@link Class} (e.g. int.class) representing the type of the supplied boxed class (e.g.
     *         {@link Integer})
     */
    public static Class getUnboxedClass(Class boxedClass)
    {
        return boxedTypeMapping.get(boxedClass);
    }

    /**
     * Finds the named method that accepts the supplied parameter types.
     *
     * @return the found method, if it exists.
     *
     * @throws NoSuchMethodException if a method with the supplied {@code name} that accepts the supplied {@code
     *                               paramTypes}.
     */
    public static Method getMethod(Class clazz, String name, Class... paramTypes) throws NoSuchMethodException
    {
        final MethodCacheKey cacheKey = createMethodCacheKey(clazz, name, paramTypes);
        Method targetMethod = methodCache.get(cacheKey);
        if (targetMethod == null) {
            targetMethod = clazz.getMethod(name, paramTypes);
            methodCache.put(cacheKey, targetMethod);
        }

        return targetMethod;
    }

    private static MethodCacheKey createMethodCacheKey(Class clazz, String name, Class[] paramTypes)
    {
        return new MethodCacheKey(clazz, name, paramTypes);
    }

    private static class MethodCacheKey
    {
        private final Class targetClass;
        private final String methodName;
        private final Class[] paramTypes;

        private MethodCacheKey(Class targetClass, String methodName, Class[] paramTypes)
        {
            this.targetClass = targetClass;
            this.methodName = methodName;
            this.paramTypes = paramTypes;
        }

        @SuppressWarnings({"SimplifiableIfStatement"})
        @Override
        public boolean equals(Object o)
        {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MethodCacheKey)) {
                return false;
            }

            final MethodCacheKey that = (MethodCacheKey) o;

            if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) {
                return false;
            }
            if (!Arrays.equals(paramTypes, that.paramTypes)) {
                return false;
            }
            return !(targetClass != null ? !targetClass.equals(that.targetClass) : that.targetClass != null);

        }

        @Override
        public int hashCode()
        {
            int result = targetClass != null ? targetClass.hashCode() : 0;
            result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
            result = 31 * result + (paramTypes != null ? Arrays.hashCode(paramTypes) : 0);
            return result;
        }
    }
}

/*
 * Copyright (c) 2007-2009 Brian Cavalier
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
package org.bc.iterate.function;

import org.bc.iterate.Function;
import org.bc.iterate.reflect.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link Function} to dynamically invoke a named parameter-less method on its {@code x} argument.
 *
 * @author Brian Cavalier
 */
public class DynamicInvokerFunction<X, Y> implements Function<X, Y>
{
    protected String methodName;

    protected Method method;

    public DynamicInvokerFunction(String methodName)
    {
        this.methodName = methodName;
    }

    @SuppressWarnings({"unchecked"})
    public Y apply(X x)
    {
        try {
            if(method == null) {
                method = ReflectionUtil.getMethod(x.getClass(), methodName);
            }
            return (Y) method.invoke(x);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

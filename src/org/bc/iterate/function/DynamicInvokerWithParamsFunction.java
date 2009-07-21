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

import org.bc.iterate.reflect.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * {@link org.bc.iterate.Function} to dynamically invoke a named method with parameters on its {@code x} argument.
 *
 * @author Brian Cavalier
 */
public class DynamicInvokerWithParamsFunction<X, Y> extends DynamicInvokerFunction<X,Y>
{
    private final Object[] params;

    @SuppressWarnings({"AssignmentToCollectionOrArrayFieldFromParameter"})
    public DynamicInvokerWithParamsFunction(String methodName, Object... params)
    {
        super(methodName);
        this.params = params;
    }

    @SuppressWarnings({"unchecked", "RefusedBequest"})
    public Y apply(X x)
    {
        try {
            if(method == null) {
                method = ReflectionUtil.getMethod(x.getClass(), methodName, params);
            }
            return (Y) method.invoke(x, params);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

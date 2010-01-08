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
package org.bc.iterate;

import org.bc.iterate.function.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Helper methods for {@link Functions}, including composition, and creating conditional {@link Functions}.
 *
 * @author Brian Cavalier
 */
public class Functions
{
    /**
     * the identity {@link org.bc.iterate.Function} for any type.
     *
     * @return the identity {@link org.bc.iterate.Function} for any type.
     */
    public static <X> Function<X, X> identity()
    {
        // IntelliJ flags this as invalid, but it is legal, and compiles/works.
        return (Function<X, X>) Identity.INSTANCE;
    }

    private enum Identity implements Function<Object, Object>
    {
        INSTANCE;

        public Object apply(Object o)
        {
            return o;
        }
    }

    /**
     * a {@link Function} that returns the number of times it has been invoked minus one.  That is, the first time it is
     * called, it returns {@code 0}, the second time {@code 1}, and so on.
     *
     * @return a {@link Function} that returns the number of times it has been invoked minus one.
     */
    public static Function<Object, Integer> index()
    {
        return new Function<Object, Integer>()
        {
            private int index = 0;

            public Integer apply(Object o)
            {
                return index++;
            }
        };
    }

    /**
     * a {@link Function} equivalent to {@code g(f)}, that is {code g.apply(f.apply(x))}.
     *
     * @param f inner {@link Function}
     * @param g outer {@link Function}
     *
     * @return a {@link Function} equivalent to {@code g(f)}, that is {code g.apply(f.apply(x))}.
     */
    public static <X, Y, Z> Function<X, Z> compose(Function<X, Y> f, Function<Y, Z> g)
    {
        return new CompositeFunction<X, Y, Z>(f, g);
    }

    public static <X> Function<X, X> compose(Function<X, X>... functions)
    {
        return compose(Arrays.asList(functions));
    }

    public static <X> Function<X, X> compose(List<Function<X, X>> functions)
    {
        return new CompositeMonoid<X>(functions);
    }

    public static <X, Y, Z, R> BinaryFunction<X, R, Z> compose(BinaryFunction<X, R, Y> f,
                                                               BinaryFunction<Y, R, Z> g)
    {
        return new CompositeBinaryFunction<X, Y, Z, R>(f, g);
    }

    public static <X, Y, Z, R> BinaryFunction<X, R, Z> compose(BinaryFunction<X, R, Y> f,
                                                               Function<Y, Z> g)
    {
        return new CompositeBinaryFunction<X, Y, Z, R>(f, Functions.<Y, R, Z>unbind(g));
    }

    public static <X, Y, Z, R> BinaryFunction<X, R, Z> compose(Function<X, Y> f, BinaryFunction<Y, R, Z> g)
    {
        return new CompositeBinaryFunction<X, Y, Z, R>(Functions.<X, R, Y>unbind(f), g);
    }

    public static <X, Y> Function<X, Y> memoize(Function<X, Y> functionToMemoize)
    {
        return new MemoizedFunction<X, Y>(functionToMemoize);
    }

    public static <X, Y> Function<X, Y> memoize(Function<X, Y> functionToMemoize, Map<X, Y> resultsCache)
    {
        return new MemoizedFunction<X, Y>(functionToMemoize, resultsCache);
    }

    public static <X, Y> Function<X, Y> memoize(Function<X, Y> functionToMemoize, int resultsCacheSize)
    {
        return new MemoizedFunction<X, Y>(functionToMemoize, resultsCacheSize);
    }

    public static <X, Y> Function<X, Y> invoke(final String methodName)
    {
        return new DynamicInvokerFunction<X, Y>(methodName);
    }

    public static <X, Y> Function<X, Y> invoke(final Method method)
    {
        return new DynamicInvokerFunction<X, Y>(method);
    }

    public static <X, Y> Function<X, Y> invoke(final String methodName, final Object... params)
    {
        return new DynamicInvokerWithParamsFunction<X, Y>(methodName, params);
    }

    public static <X, Y> Function<X, Y> invoke(final Method method, final Object... params)
    {
        return new DynamicInvokerWithParamsFunction<X, Y>(method, params);
    }

    public static <X, Y, Z> Function<X, Z> bind(final Y paramToBind, final BinaryFunction<X, Y, Z> callee)
    {
        return new Function<X, Z>()
        {
            public Z apply(X x)
            {
                return callee.apply(x, paramToBind);
            }
        };
    }

    public static <X, Y, Z> BinaryFunction<X, Y, Z> unbind(final Function<X, Z> callee)
    {
        return new BinaryFunction<X, Y, Z>()
        {
            public Z apply(X x, Y y)
            {
                return callee.apply(x);
            }
        };
    }

    public static <X> Function<X, X> conditional(final Condition<X> condition, final Function<X, X> f)
    {
        return new Function<X, X>()
        {
            public X apply(X x)
            {
                return condition.eval(x) ? f.apply(x) : x;
            }
        };
    }

    public static <X, Y> Function<X, Y> conditional(final Condition<X> condition,
                                                    final Function<X, Y> f1,
                                                    final Function<X, Y> f2)
    {
        return new Function<X, Y>()
        {
            public Y apply(X x)
            {
                return condition.eval(x) ? f1.apply(x) : f2.apply(x);
            }
        };
    }

    public static <X, Y> Function<X, Y> conditional(final Function<X, Integer> selector,
                                                    final List<Function<X, Y>> functions)
    {
        return new Function<X, Y>()
        {
            public Y apply(X x)
            {
                return functions.get(selector.apply(x) % functions.size()).apply(x);
            }
        };
    }

    public static <X, Y> Function<X, Y> conditional(final Function<X, Integer> selector,
                                                    final Function<X, Y>... functions)
    {
        return conditional(selector, Arrays.asList(functions));
    }

    public static <X, Y, F extends Function<? super X, ? extends Y>>
    BinaryFunction<? super X, List<F>, Y>conditional(final BinaryFunction<X, List<F>, F> conditional)
    {
        return new BinaryFunction<X, List<F>, Y>()
        {
            @Override
            public Y apply(X x, List<F> functions)
            {
                return conditional.apply(x, functions).apply(x);
            }
        };
    }

    public static <X, Y> Function<X, Y> conditional(final Function<X, Function<? super X, ? extends Y>> conditional)
    {
        return new Function<X, Y>()
        {
            @Override
            public Y apply(X x)
            {
                return conditional.apply(x).apply(x);
            }
        };
    }

    public static <X, Y> Function<X, Y> conditional(final Provider<Function<? super X, ? extends Y>> functionProvider)
    {
        return new Function<X, Y>()
        {
            @Override
            public Y apply(X x)
            {
                return functionProvider.get().apply(x);
            }
        };
    }

    /**
     * a {@link Function} that will apply {@code f} to each element of an {@link Iterable}, returning an {@link
     * Iterable} containing all corresponding results.
     *
     * @param f {@link Function} to apply to each element
     *
     * @return a {@link Function} that will apply {@code f} to each element of an {@link Iterable}, returning an {@link
     *         Iterable} containing all corresponding results.
     */
    public static <X, Y> Function<Iterable<X>, Iterable<Y>> applyToEach(final Function<? super X, ? extends Y> f)
    {
        return new Function<Iterable<X>, Iterable<Y>>()
        {
            @Override
            public Iterable<Y> apply(Iterable<X> xIterable)
            {
                List<Y> results = new ArrayList<Y>(Iterate.estimateSize(xIterable));
                for (X x : xIterable) {
                    results.add(f.apply(x));
                }

                return results;
            }
        };
    }
}

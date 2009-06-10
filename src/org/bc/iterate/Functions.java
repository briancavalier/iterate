/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.CompositeBinaryFunction;
import org.bc.iterate.function.CompositeFunction;
import org.bc.iterate.function.CompositeMonoid;
import org.bc.iterate.function.Identity;

import java.util.Arrays;
import java.util.List;

public class Functions
{
    public static <X> Function<X, X> identity()
    {
        return new Identity<X>();
    }

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

    public static <X> Function<X, X> conditional(final Condition<X> condition, final Function<X, X> f1, final Function<X, X> f2)
    {
        return new Function<X, X>()
        {
            public X apply(X x)
            {
                return condition.eval(x) ? f1.apply(x) : f2.apply(x);
            }
        };
    }
}

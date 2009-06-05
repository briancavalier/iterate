/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.CompositeFunction;
import org.bc.iterate.function.CompositeMonoid;

import java.util.Arrays;
import java.util.List;

public class Functions
{
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
}

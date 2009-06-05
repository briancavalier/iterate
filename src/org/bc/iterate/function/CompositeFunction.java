/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.Function;

public class CompositeFunction<X, Y, Z> implements Function<X, Z>
{
    final private Function<X,Y> f;
    final private Function<Y,Z> g;

    public CompositeFunction(final Function<X, Y> f, final Function<Y, Z> g)
    {
        this.f = f;
        this.g = g;
    }

    public Z apply(X x)
    {
        return g.apply(f.apply(x));
    }
}

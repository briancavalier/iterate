/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.BinaryFunction;

public class CompositeBinaryFunction<X, Y, Z, R> implements BinaryFunction<X, R, Z>
{
    final private BinaryFunction<X, R, Y> f;
    final private BinaryFunction<Y, R, Z> g;

    public CompositeBinaryFunction(BinaryFunction<X, R, Y> f,
                                   BinaryFunction<Y, R, Z> g)
    {
        this.f = f;
        this.g = g;
    }

    public Z apply(X x, R r)
    {
        return  g.apply(f.apply(x, r), r);
    }
}

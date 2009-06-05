/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.Function;

import java.util.List;

public class CompositeMonoid<X> implements Function<X, X>
{
    final private List<Function<X, X>> functions;

    public CompositeMonoid(final List<Function<X, X>> functions)
    {
        this.functions = functions;
    }

    public X apply(X x)
    {
        X result = x;
        for (Function<X, X> f : functions) {
            result = f.apply(result);
        }

        return result;
    }
}
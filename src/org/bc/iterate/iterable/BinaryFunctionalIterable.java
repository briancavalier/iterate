/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import org.bc.iterate.BinaryFunction;

import java.util.Iterator;

public class BinaryFunctionalIterable<X, Y, Z> extends IterableBase<Y>
{
    private final Iterator<X> xItems;

    private Z param;

    private final BinaryFunction<? super X, ? super Z, Y> f;

    public BinaryFunctionalIterable(Iterable<X> xItems, Z param, BinaryFunction<? super X, ? super Z, Y> f)
    {
        this.xItems = xItems.iterator();
        this.f = f;
        this.param = param;
    }

    public boolean hasNext()
    {
        return xItems.hasNext();
    }

    public Y next()
    {
        return f.apply(xItems.next(), param);
    }

}

/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import org.bc.iterate.Function;

import java.util.Iterator;

public class FunctionalIterable<X, Y> extends IterableBase<Y>
{
    private final Iterator<X> xItems;

    private final Function<? super X, ? extends Y> f;

    public FunctionalIterable(Iterable<X> xItems, Function<? super X, ? extends Y> f)
    {
        this.xItems = xItems.iterator();
        this.f = f;
    }

    public boolean hasNext()
    {
        return xItems.hasNext();
    }

    public Y next()
    {
        return f.apply(xItems.next());
    }
}

/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import java.util.Iterator;

public class FlatteningIterable<X> extends IterableBase<X>
{
    final private Iterator<? extends Iterable<X>> nested;
    private Iterator<X> current;

    public FlatteningIterable(Iterable<? extends Iterable<X>> nested)
    {
        this.nested = nested.iterator();
        this.current = this.nested.next().iterator();
    }

    public boolean hasNext()
    {
        if (current.hasNext()) {
            return true;
        }

        if(nested.hasNext()) {
            current = nested.next().iterator();
            return true;
        }

        return false;
    }

    public X next()
    {
        return current.next();
    }

    @SuppressWarnings({"RefusedBequest"})
    @Override
    public void remove()
    {
        current.remove();
    }
}

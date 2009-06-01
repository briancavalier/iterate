/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import org.bc.iterate.Condition;

import java.util.Iterator;

public class AfterIterable<X> extends FilterIterable<X>
{
    private boolean found = false;

    public AfterIterable(Iterable<X> items, Condition<? super X> condition)
    {
        super(items, condition);
    }

    @SuppressWarnings({"RefusedBequest"})
    @Override
    protected X findNext(final Iterator<X> iterator, final Condition<? super X> filter)
    {
        if (!found && iterator.hasNext()) {
            found = filter.eval(iterator.next());
        }

        X next = null;
        while (iterator.hasNext()) {
            next = iterator.next();
            if (filter.eval(next)) {
                next = null;
                found = true;
            }
        }
        return next;
    }
}

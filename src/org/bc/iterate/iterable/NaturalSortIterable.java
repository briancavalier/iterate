/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NaturalSortIterable<X> implements Iterable<X>
{
    final private List<X> sorted;

    public NaturalSortIterable(final Iterable<X> iterable)
    {
        // This pretty much stinks, but is necessary because we don't want to force X to implement Comparable
        // at the Iterate class level.
        this.sorted = new ArrayList<X>(100);
        for (X x : iterable) {
            this.sorted.add(x);
        }

        if(sorted.size() > 0) {
            X x = sorted.get(0);
            if(x instanceof Comparable) {
                Collections.sort((List<? extends Comparable>)sorted);
            } else {
                throw new IllegalStateException("Parameterized type X does not implement Comparable, and cannot be sorted by"
                                                + "natural order.  You must use sorted(Comparator) instead of sorted()");
            }
        }
    }

    public Iterator<X> iterator()
    {
        return sorted.iterator();
    }
}

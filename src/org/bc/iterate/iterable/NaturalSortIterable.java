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
    private List<X> sorted;
    private Iterable<X> source;

    public NaturalSortIterable(final Iterable<X> source)
    {
        this.source = source;
    }

    public Iterator<X> iterator()
    {
        if(sorted == null) {
            // This pretty much stinks, but is necessary because we don't want to force X to implement Comparable
            // at the Iterate class level.
            this.sorted = new ArrayList<X>(100);
            for (X x : source) {
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

        return sorted.iterator();
    }
}

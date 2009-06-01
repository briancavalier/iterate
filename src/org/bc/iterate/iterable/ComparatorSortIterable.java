/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import java.util.*;

public class ComparatorSortIterable<X> implements Iterable<X>
{
    final private List<X> sorted;

    public ComparatorSortIterable(final Iterable<X> iterable, final Comparator<X> comparator)
    {
        sorted = new ArrayList<X>(100);
        for (X x : iterable) {
            sorted.add(x);
        }

        Collections.sort(sorted, comparator);
    }

    public Iterator<X> iterator()
    {
        return sorted.iterator();
    }
}
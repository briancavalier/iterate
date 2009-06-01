/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.iterable;

import java.util.*;

public class ComparatorSortIterable<X> implements Iterable<X>
{
    private List<X> sorted;
    private Iterable<X> source;
    private Comparator<X> comparator;

    public ComparatorSortIterable(final Iterable<X> source, final Comparator<X> comparator)
    {
        this.source = source;
        this.comparator = comparator;
    }

    public Iterator<X> iterator()
    {
        if (source == null) {
            sorted = new ArrayList<X>(100);
            for (X x : source) {
                sorted.add(x);
            }

            Collections.sort(sorted, comparator);
        }
        return sorted.iterator();
    }
}
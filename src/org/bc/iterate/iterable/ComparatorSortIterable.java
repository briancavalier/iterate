/*
 * Copyright (c) 2007-2010 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bc.iterate.iterable;

import org.bc.iterate.Iterables;
import org.bc.iterate.Iterate;

import java.util.*;

/**
 * An {@link Iterable} that presents a sorted view of the items in another {@link Iterable} using a {@link Comparator}
 *
 * @author Brian Cavalier
 */
public class ComparatorSortIterable<X> implements Iterable<X>
{
    private List<X> sorted;
    private final Iterable<X> source;
    private final Comparator<X> comparator;

    public ComparatorSortIterable(final Iterable<X> source, final Comparator<X> comparator)
    {
        this.source = source;
        this.comparator = comparator;
    }

    public Iterator<X> iterator()
    {
        if (sorted == null) {
            sorted = Iterables.addAll(new ArrayList<X>(Iterate.estimateSize(source)), source);
            Collections.sort(sorted, comparator);
        }
        return sorted.iterator();
    }
}
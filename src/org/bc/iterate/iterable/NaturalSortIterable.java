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

import org.bc.iterate.HasEstimatedSize;
import org.bc.iterate.Iterables;
import org.bc.iterate.Iterate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} that presents a sorted view of the items in another {@link Iterable} using a the items'
 * natural ordering.
 *
 * @author Brian Cavalier
 */
public class NaturalSortIterable<X extends Comparable<X>> implements Iterable<X>, HasEstimatedSize
{
    private List<X> sorted;
    private Iterable<X> source;

    public NaturalSortIterable(final Iterable<X> source)
    {
        this.source = source;
    }

    public Iterator<X> iterator()
    {
        // This method and getEstimatedSize() may need to be synchronized
        if (sorted == null) {
            final List<X> tmp = Iterables.addAll(new ArrayList<X>(Iterate.estimateSize(source)), source);
            Collections.sort(tmp);
            // Ensure that underlying sorted list cannot be modified via Iterator.remove()
            sorted = Collections.unmodifiableList(tmp);
        }

        return sorted.iterator();
    }

    @Override
    public int getEstimatedSize()
    {
        return sorted == null ? Iterate.estimateSize(source) : sorted.size();
    }
}

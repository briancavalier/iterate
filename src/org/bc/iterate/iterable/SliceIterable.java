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
import org.bc.iterate.Iterate;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SliceIterable<X> implements Iterable<X>, HasEstimatedSize
{
    private final Iterable<X> items;
    private final int start;
    private final int end;

    public SliceIterable(final Iterable<X> items, final int start, final int end)
    {
        if(start < 0 || start >= end) {
            throw new IllegalArgumentException("start must be >= 0 and < end");
        }
        
        this.items = items;
        this.start = start;
        this.end = end;
    }

    public SliceIterable(final Iterable<X> items, final int start)
    {
        if(start < 0) {
            throw new IllegalArgumentException("start must be >= 0");
        }

        this.items = items;
        this.start = start;
        this.end = -1;
    }

    @Override
    public Iterator<X> iterator()
    {
        return new SliceIterator();
    }

    @Override
    public int getEstimatedSize()
    {
        return Math.min(end, Iterate.estimateSize(items)) - start;
    }

    private class SliceIterator extends AbstractIterator<X>
    {
        private final Iterator<X> iterator = items.iterator();
        private int index = 0;

        public boolean hasNext()
        {
            try {
                while(index < start && iterator.hasNext()) {
                    index++;
                    iterator.next();
                }
            } catch(NoSuchElementException ignored) {
                return false;
            }

            return (index >= start && index != end && iterator.hasNext());
        }

        public X next()
        {
            if(end == -1 || index < end) {
                index++;
                return iterator.next();
            }

            throw new NoSuchElementException("Iterator is beyond slice range end: iter=" + index +
                                             ", range=(" + start + ", " + end + ")");
        }
    }
}

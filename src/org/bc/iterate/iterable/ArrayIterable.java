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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterable<X> implements Iterable<X>, HasEstimatedSize
{
    private final X[] array;
    private final int end;
    private final int start;

    public ArrayIterable(final X[] array)
    {
        this(array, 0, array.length);
    }

    public ArrayIterable(X[] array, int start, int end)
    {
        if(start < 0) {
            throw new IllegalArgumentException("start must be >= 0");
        }
        if(end > array.length) {
            throw new IllegalArgumentException("end must be < array.length");
        }
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter
        this.array = array;
        this.end = end;
        this.start = start;
    }

    @Override
    public Iterator<X> iterator()
    {
        return new ArrayIterator();
    }

    @Override
    public String toString()
    {
        return "[" + array[0] + ".." + array[array.length-1] + ']';
    }

    @Override
    public int getEstimatedSize()
    {
        return end - start;
    }

    private class ArrayIterator implements Iterator<X>
    {
        private int index = start;

        public boolean hasNext()
        {
            return index < end;
        }

        public X next()
        {
            if(hasNext()) {
                return array[index++];
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
}

/*
 * Copyright 2007-2009 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bc.iterate.iterable;

import org.bc.iterate.Condition;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilterIterable<X> extends IterableBase<X>
{
    private final Condition<? super X> filter;
    private final Iterator<X> iterator;
    private X next;

    public FilterIterable(Iterable<X> iterable, Condition<? super X> filter)
    {
        this.filter = filter;
        this.iterator = iterable.iterator();
    }

    public boolean hasNext()
    {
        next = findNext(iterator, filter);
        return next != null;
    }

    protected X findNext(final Iterator<X> iterator, final Condition<? super X> filter)
    {
        while(iterator.hasNext()) {
            X item = iterator.next();
            if(filter.eval(item)) {
                return item;
            }
        }

        return null;
    }

    public X next()
    {
        if(next == null) {
            next = findNext(iterator, filter);
            if(next == null) {
                throw new NoSuchElementException();
            }
        }

        return next;
    }

    @SuppressWarnings({"RefusedBequest"})
    public void remove()
    {
        iterator.remove();
    }
}

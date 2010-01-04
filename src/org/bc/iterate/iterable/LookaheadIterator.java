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

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class LookaheadIterator<T> extends AbstractIterator<T>
{
    protected T next;

    @Override
    public final boolean hasNext()
    {
        if (next == null) {
            next = findNext();
        }

        return (next != null);
    }

    @Override
    public final T next()
    {
        if (next == null) {
            throw new NoSuchElementException();
        }

        T current = next;
        next = findNext();

        return current;
    }

    /**
     * Returns a type safe indicator that {@link #findNext()} cannot find any more items, and any subsequent
     * calls to {@link #findNext()} will also return the value of {@code end()}.
     *
     * @return In practice, this method currently always returns {@code null}.
     */
    protected final T end()
    {
        return null;
    }

    /**
     * Subclasses should implement this method to return the next available item, or {@link #end()} if no more
     * items are available.
     *
     * @return next item or {@link #end()}
     */
    protected abstract T findNext();
}
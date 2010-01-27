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
package org.bc.iterate;

import java.util.Iterator;

public class BasicIterateImpl<X> extends Iterate<X>
{
    protected Iterable<X> iterable;

    protected BasicIterateImpl()
    {

    }
    
    protected BasicIterateImpl(Iterable<X> items)
    {
        this(items, estimateSize(items));
    }

    protected BasicIterateImpl(Iterable<X> items, int estimatedSize)
    {
        super(estimatedSize);
        this.iterable = items;
    }

    /**
     * {@link java.util.Iterator} over items in this {@link org.bc.iterate.Iterate} view.  Typically, callers will not use this method, but
     * instead will use the internal iteration methods {@code each}, {@code map}, {@code visit}, and {@code reduce}.
     *
     * @return {@link java.util.Iterator} over items in this {@link org.bc.iterate.Iterate} view.  Typically, callers will not use this method,
     *         but instead will use the internal iteration methods {@code each}, {@code map}, {@code visit}, and {@code
     *         reduce}.
     */
    public Iterator<X> iterator()
    {
        return iterable.iterator();
    }

    public String toString()
    {
        return iterable.toString();
    }
}

/*
 * Copyright (c) 2007-2009 Brian Cavalier
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

import org.bc.iterate.iterable.FlattenIterable;

import java.util.Iterator;

public class Iterables
{
    public static <X> Function<Iterable<Iterable<X>>, Iterable<X>> flatten()
    {
        return new Function<Iterable<Iterable<X>>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<Iterable<X>> nested)
            {
                return new FlattenIterable<X>(nested);
            }
        };
    }

    /**
     * Unfortunately necessary method that wraps an {@link java.util.Iterator} as an {@link Iterable}.  Ideally, this
     * would simply be another variant of {@code each()}, but that makes {@code each(Iterable)} and {@code each(Iterator)} ambiguous.
     *
     * @param items {@link java.util.Iterator} to wrap as an {@link Iterable}
     * @return {@link Iterable} whose {@link Iterable#iterator()} method will return {@code items}
     */
    public static <X> Iterable<X> of(final Iterator<X> items)
    {
        return new Iterable<X>()
        {
            public Iterator<X> iterator()
            {
                return items;
            }
        };
    }
}

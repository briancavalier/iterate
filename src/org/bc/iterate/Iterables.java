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

import org.bc.iterate.iterable.ConcatIterable;
import org.bc.iterate.iterable.GeneratorIterable;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Provides {@link Iterables}, {@link Functions}, and helper methods for working with {@link Iterables}.
 */
public class Iterables
{
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

    /**
     * @param n number of items to generate
     * @param generator {@link Function} which will be used by the returned {@link Iterable} to generate items.
     * @return an {@link Iterable} which will invoke the supplied {@link Function} exactly {@code n} times.
     */
    public static <X> Iterable<X> generate(int n, Function<Integer, X> generator)
    {
        return new GeneratorIterable<X>(n, generator);
    }

    /**
     * @return a {@link Function} that will return a single {@link Iterable} representing a concatenation
     *         of the supplied nested {@link Iterable}s.
     */
    public static <X> Function<Iterable<Iterable<X>>, Iterable<X>> concat()
    {
        return new Function<Iterable<Iterable<X>>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<Iterable<X>> nested)
            {
                return new ConcatIterable<X>(nested);
            }
        };
    }

    /**
     * Returns a flattened view of the nested {@link Iterable}s, such that calling next() after the last item of the
     * first nested {@link Iterable} yields the first item of the second {@link Iterable}, calling next() again yields
     * the second item of the second {@link Iterable}, and so on.
     *
     * @param nested 2D {@link Iterable} of {@link Iterable}
     *
     * @return an {@link Iterable} over items in each of the supplied {@link Iterable}s, which return items from the
     * first {@link Iterable} until exhausted followed by items from the second {@link Iterable} until exhausted, and
     * so on.
     */
    public static <X, Y extends Iterable<X>> Iterable<X> concat(final Iterable<Y> nested)
    {
        return new ConcatIterable<X>(nested);
    }

    /**
     * Returns a flattened view of the nested {@link Iterable}s, such that calling next() after the last item of the
     * first nested {@link Iterable} yields the first item of the second {@link Iterable}, calling next() again yields
     * the second item of the second {@link Iterable}, and so on.
     *
     * @param nested array of {@link Iterable}
     * 
     * @return an {@link Iterable} over items in each of the supplied {@link Iterable}s, which return items from the
     * first {@link Iterable} until exhausted followed by items from the second {@link Iterable} until exhausted, and
     * so on.
     */
    public static <X> Iterable<X> concat(Iterable<X>... nested)
    {
        return new ConcatIterable<X>(Arrays.asList(nested));
    }
}

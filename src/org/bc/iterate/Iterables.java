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

import org.bc.iterate.iterable.*;

import java.util.*;

/**
 * Provides {@link Iterables}, {@link Functions}, and helper methods for working with {@link Iterables}.
 *
 * @author Brian Cavalier
 */
public class Iterables
{
    /**
     * @param n         number of items to generate
     * @param generator {@link Function} which will be used by the returned {@link Iterable} to generate items.
     * @return an {@link Iterable} which will invoke the supplied {@link Function} exactly {@code n} times.
     */
    public static <X> Iterate<X> generate(int n, Function<Integer, X> generator)
    {
        return Iterate.each(new GeneratorIterable<X>(n, generator));
    }

    public static <X> Iterate<X> generate(int n, Provider<X> provider)
    {
        final int n1 = n;
        final Provider<X> provider1 = provider;

        return Iterate.each(new Iterable<X>() {

            @Override
            public Iterator<X> iterator()
            {
                return new LookaheadIterator<X>()
                {
                    private final int end = n1;
                    private final Provider<X> provider = provider1;
                    private int index = 0;

                    @Override
                    protected X findNext()
                    {
                        if (index >= end) {
                            return end();
                        } else {
                            X x = provider.get();
                            return x == null ? end() : x;
                        }
                    }
                };
            }
        });
    }

    public static <X> Iterable<X> generate(final Provider<X> provider)
    {
        return new Iterable<X>()
        {
            @Override
            public Iterator<X> iterator()
            {
                return new LookaheadIterator<X>()
                {
                    @Override
                    protected X findNext()
                    {
                        final X x = provider.get();
                        return x == null ? end() : x;
                    }
                };
            }
        };
    }

    /**
     * @param iterable {@link Iterable} containing items to cycle through forever.
     * @return an inexhaustible {@link Iterable} that iterates over all items in {@code iterable} from beginning
     *         to end, then returns to the beginning, etc.
     */
    public static <X> Iterable<X> cycle(final Iterable<X> iterable)
    {
        return new Iterable<X>()
        {
            @Override
            public Iterator<X> iterator()
            {
                return new AbstractIterator<X>()
                {
                    private Iterator<X> iterator = iterable.iterator();

                    public boolean hasNext()
                    {
                        return true;
                    }

                    public X next()
                    {
                        if (!iterator.hasNext()) {
                            iterator = iterable.iterator();
                        }
                        return iterator.next();
                    }
                };
            }
        };
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
     * @return an {@link Iterable} over items in each of the supplied {@link Iterable}s, which return items from the
     *         first {@link Iterable} until exhausted followed by items from the second {@link Iterable} until exhausted, and
     *         so on.
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
     * @return an {@link Iterable} over items in each of the supplied {@link Iterable}s, which return items from the
     *         first {@link Iterable} until exhausted followed by items from the second {@link Iterable} until exhausted, and
     *         so on.
     */
    public static <X> Iterable<X> concat(Iterable<X>... nested)
    {
        return new ConcatIterable<X>(Arrays.asList(nested));
    }

    public static <X> Iterable<X> sorted(Iterable<X> iterable, Comparator<X> comparator)
    {
        return new ComparatorSortIterable<X>(iterable, comparator);
    }

    public static <X extends Comparable<X>> Iterable<X> sorted(Iterable<X> iterable)
    {
        return new NaturalSortIterable<X>(iterable);
    }

    /**
     * Adds all the items in {@code itemsToAdd} to {@code collection}
     *
     * @param collection {@link java.util.Collection} to which items will be added
     * @param itemsToAdd items to be added
     * @return {@code collection}
     */
    public static <X, C extends Collection<X>> C addAll(C collection, Iterable<X> itemsToAdd)
    {
        for (X x : itemsToAdd) {
            collection.add(x);
        }
        return collection;
    }

    public static <X> Function<Iterable<X>, Iterable<X>> prepend(final Iterable<X> toPrepend)
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                return new ConcatIterable<X>(toPrepend, xIterable);
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> append(final Iterable<X> toAppend)
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                return new ConcatIterable<X>(xIterable, toAppend);
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> slice(final int start, final int end)
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                return new SliceIterable<X>(xIterable, start, end);
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> slice(final int start)
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                return new SliceIterable<X>(xIterable, start);
            }
        };
    }

    public static <X extends Comparable<X>> Function<Iterable<X>, Iterable<X>> sort()
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                final List<X> buffer = addAll(new ArrayList<X>(Iterate.estimateSize(xIterable)), xIterable);
                Collections.sort(buffer);
                return buffer;
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> sort(final Comparator<X> comparator)
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                final List<X> buffer = addAll(new ArrayList<X>(Iterate.estimateSize(xIterable)), xIterable);
                Collections.sort(buffer, comparator);
                return buffer;
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> shuffle()
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                final List<X> buffer = addAll(new ArrayList<X>(Iterate.estimateSize(xIterable)), xIterable);
                Collections.shuffle(buffer);
                return buffer;
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> shuffle(final Random random)
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                final List<X> buffer = addAll(new ArrayList<X>(Iterate.estimateSize(xIterable)), xIterable);
                Collections.shuffle(buffer, random);
                return buffer;
            }
        };
    }

    public static <X> Function<Iterable<X>, Iterable<X>> reverse()
    {
        return new Function<Iterable<X>, Iterable<X>>()
        {
            public Iterable<X> apply(Iterable<X> xIterable)
            {
                final List<X> buffer = addAll(new ArrayList<X>(Iterate.estimateSize(xIterable)), xIterable);
                Collections.reverse(buffer);
                return buffer;
            }
        };
    }

    static <X> Iterable<X> of(final Iterator<X> items)
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

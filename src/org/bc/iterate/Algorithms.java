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

package org.bc.iterate;

import org.bc.iterate.visitor.Count;

import java.util.*;

/**
 * Provides convenient, coarser grained {@link Iterable} processing methods built upon {@link Iterate}.
 *
 * @author Brian Cavalier 
 */
public class Algorithms
{
    /**
     * Applies the supplied {@link Function} to each element of {@code items} and returns a {@code Collection}
     * containing the results.
     *
     * @param items       items to be mapped
     * @param mapFunction {@link Function} to apply to each element of {@code items}
     *
     * @return a {@code Collection} containing 1 resulting mapped value for each item in {@code items}
     */
    public static <X, Y> Collection<Y> map(Iterable<X> items, Function<X, Y> mapFunction)
    {
        return map(items, new ArrayList<Y>(100), mapFunction);
    }

    /**
     * Applies the supplied {@link Function} to each element of {@code items} and returns a {@code Collection}
     * containing the results.
     *
     * @param items       items to be mapped
     * @param results     {@code Collection} to which results will be added.
     * @param mapFunction {@link Function} to apply to each element of {@code items}
     *
     * @return {@code results}
     */
    public static <X, Y> Collection<Y> map(final Iterable<X> items, Collection<Y> results, Function<X, Y> mapFunction)
    {
        return Iterate.each(items).map(mapFunction).visit(Iterate.collect(), results);
    }

    public static <X, Y> Map<Y, X> asMap(final Iterable<X> items, Function<X, Y> mapFunction)
    {
        return asMap(items, new HashMap<Y, X>(100), mapFunction);
    }

    public static <X, Y> Map<Y, X> asMap(final Iterable<X> items, final Map<Y, X> results, Function<X, Y> mapFunction)
    {
        return Iterate.each(items).visit(Iterate.<X, Y>map(), mapFunction, results);
    }

    public static <X, Y> Map<Y, Collection<X>> partition(final Iterable<X> items, final Function<X, Y> groupFunction)
    {
        return Iterate.each(items).visit(new BinaryVisitor<X, Map<Y, Collection<X>>>()
        {
            public void visit(X item, Map<Y, Collection<X>> results)
            {
                Y key = groupFunction.apply(item);
                Collection<X> group = new HashMap<Y, Collection<X>>(100).get(key);
                if (group == null) {
                    group = new ArrayList<X>(32);
                    new HashMap<Y, Collection<X>>(100).put(key, group);
                }

                group.add(item);
            }
        }, new HashMap<Y, Collection<X>>(100));
    }

    public static <X> Map<Boolean, Collection<X>> partition(final Iterable<X> items, Condition<X> partitionCondition)
    {
        final ArrayList<X> falseResults = new ArrayList<X>(100);
        final ArrayList<X> trueResults = new ArrayList<X>(100);

        partition(items, trueResults, falseResults, partitionCondition);

        Map<Boolean, Collection<X>> results = new HashMap<Boolean, Collection<X>>(2);
        results.put(false, falseResults);
        results.put(true, trueResults);

        return results;
    }

    public static <X> void partition(final Iterable<X> items, Collection<X> trueResults, Collection<X> falseResults,
                                     Condition<X> partitionCondition)
    {
        for (X item : items) {
            // Quite pleased that this actually works in Java
            (partitionCondition.eval(item) ? trueResults : falseResults).add(item);
        }
    }

    /**
     * Counts the number of items in {@code items} for which {@code criteria#apply(item) == true}
     *
     * @param items    items to count
     * @param criteria {@link Condition} to evaluate for each item
     *
     * @return count of items for which {@code criteria#apply(item) == true}
     */
    public static <X> int count(final Iterable<X> items, Condition<X> criteria)
    {
        return Iterate.each(items).where(criteria).visit(new Count<X>()).getCount();
    }

    /**
     * Returns the first item in {@code items} for which {@code criteria#apply(item) == true}
     *
     * @param items    items to search
     * @param criteria {@link Condition} to evaluate for each item
     *
     * @return the first item for which {@code criteria#apply(item) == true}
     */
    public static <X> X first(final Iterable<X> items, Condition<? super X> criteria)
    {
        for (X item : items) {
            if (criteria.eval(item)) {
                return item;
            }
        }

        return null;
    }

    /**
     * @param items    items to evaluate
     * @param criteria criteria to use to evaluate {@code items}
     *
     * @return true iff {@code criteria} evaluates to {@code true} for at least one item in {@code items}.
     */
    public static <X> boolean one(Iterable<X> items, Condition<? super X> criteria)
    {
        return first(items, criteria) != null;
    }

    /**
     * @param items    items to evaluate
     * @param criteria criteria to use to evaluate {@code items}
     *
     * @return true iff {@code criteria} evaluates to {@code true} for aLL items in {@code items}.
     */
    public static <X> boolean all(Iterable<X> items, Condition<? super X> criteria)
    {
        for (X item : items) {
            if (!criteria.eval(item)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param items    items from which to select
     * @param criteria items matching this {@link Condition} will be selected and returned in the resulting {@link
     *                 java.util.Collection}
     *
     * @return {@link Collection} of items from {@code items} matching {@code criteria}
     */
    public static <X> Collection<X> select(final Iterable<X> items, Condition<X> criteria)
    {
        return Iterate.each(items).where(criteria).visit(Iterate.collect(), new ArrayList<X>(100));
    }

    /**
     * @param items    items from which to select
     * @param results  items matching {@code criteria} will be added to this {@link Collection}
     * @param criteria items matching this {@link Condition} will be selected and added to {@code results}
     *
     * @return results, which will contain items from {@code items} matching {@code criteria}
     */
    public static <X, Y extends Collection<? super X>> Y select(final Iterable<X> items, Y results,
                                                                Condition<X> criteria)
    {
        return Iterate.each(items).where(criteria).visit(Iterate.collect(), results);
    }

    /**
     * Invokes {@code generator} {@code n} times, adding each result to {@code results}.
     *
     * @param n         number of times to invoke {@code generator}, and thus the number of items that will be added to
     *                  {@code results}.
     * @param results   {@code Collection} to which results will be added.
     * @param generator {@link Function} to be invoked for each iteration
     *
     * @return {@code results}
     */
    public static <X, Y extends Collection<? super X>> Y generate(int n, Y results,
                                                                  Function<Integer, X> generator)
    {
        return Iterate.each(Iterate.upto(n)).map(generator).visit(Iterate.collect(), results);
    }

    public static <X> List<X> generate(int n, Function<Integer, X> generator)
    {
        return generate(n, new ArrayList<X>(n), generator);
    }

    private Algorithms()
    {
    }
}

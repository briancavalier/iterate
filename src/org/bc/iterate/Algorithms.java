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

import org.bc.iterate.function.Index;
import org.bc.iterate.relational.Join;
import org.bc.iterate.relational.JoinResult;
import org.bc.iterate.util.Pair;
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
     * @return a {@code List} containing 1 resulting mapped value for each item in {@code items}
     */
    public static <X, Y> List<Y> map(Iterable<X> items, Function<X, Y> mapFunction)
    {
        return map(items, new ArrayList<Y>(Iterate.estimateSize(items)), mapFunction);
    }

    /**
     * Applies the supplied {@link Function} to each element of {@code items} and returns a {@code Collection}
     * containing the results.
     *
     * @param items       items to be mapped
     * @param results     {@code Collection} to which results will be added.
     * @param mapFunction {@link Function} to apply to each element of {@code items}
     * @return {@code results}
     */
    public static <X, Y, CollectionType extends Collection<? super Y>> CollectionType map(final Iterable<X> items, CollectionType results, Function<X, Y> mapFunction)
    {
        return Iterate.each(items).map(mapFunction).visit(Iterate.collect(), results);
    }

    public static <X, Y> Map<Y, X> asMap(final Iterable<X> items, Function<X, Y> mapFunction)
    {
        return asMap(items, new HashMap<Y, X>(Iterate.estimateSize(items)), mapFunction);
    }

    public static <X, Y, MapType extends Map<Y, X>> MapType asMap(final Iterable<X> items, final MapType results, Function<X, Y> mapFunction)
    {
        return Iterate.each(items).visit(Iterate.map(mapFunction), results);
    }

    public static <X, Y, CollectionType extends Collection<JoinResult<Integer, X, Y>>> CollectionType zip(final Iterable<X> left, final Iterable<Y> right, CollectionType results)
    {
        return Iterate.each(left).join(Join.left(new Index(), new Index()), right).add(results);
    }

    public static <X, Y> List<JoinResult<Integer, X, Y>> zip(final Iterable<X> left, final Iterable<Y> right)
    {
        return zip(left, right, new ArrayList<JoinResult<Integer, X, Y>>(Iterate.estimateSize(left)));
    }

    public static <X, Y> Map<Y, Collection<X>> partition(final Iterable<X> items, final Function<X, Y> groupFunction)
    {
        final int size = Iterate.estimateSize(items);
        final int groupSize = (size / 2) + 1;
        return Iterate.each(items).visit(new BinaryVisitor<X, Map<Y, Collection<X>>>()
        {
            public void visit(X item, Map<Y, Collection<X>> results)
            {
                Y key = groupFunction.apply(item);
                Collection<X> group = results.get(key);
                if (group == null) {
                    group = new ArrayList<X>(groupSize);
                    results.put(key, group);
                }

                group.add(item);
            }
        }, new HashMap<Y, Collection<X>>(size));
    }

    public static <X> Map<Boolean, Collection<X>> partition(final Iterable<X> items, Condition<X> partitionCondition)
    {
        final int size = (Iterate.estimateSize(items) / 2) + 1;
        final ArrayList<X> falseResults = new ArrayList<X>(size);
        final ArrayList<X> trueResults = new ArrayList<X>(size);

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
     * @return true iff {@code criteria} evaluates to {@code true} for at least one item in {@code items}.
     */
    public static <X> boolean one(Iterable<X> items, Condition<? super X> criteria)
    {
        return first(items, criteria) != null;
    }

    /**
     * @param items    items to evaluate
     * @param criteria criteria to use to evaluate {@code items}
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
     * @return {@link Collection} of items from {@code items} matching {@code criteria}
     */
    public static <X> Collection<X> select(final Iterable<X> items, Condition<X> criteria)
    {
        return Iterate.each(items).where(criteria).visit(Iterate.collect(), new ArrayList<X>(Iterate.estimateSize(items)));
    }

    /**
     * @param items    items from which to select
     * @param results  items matching {@code criteria} will be added to this {@link Collection}
     * @param criteria items matching this {@link Condition} will be selected and added to {@code results}
     * @return results, which will contain items from {@code items} matching {@code criteria}
     */
    public static <X, CollectionType extends Collection<? super X>> CollectionType select(final Iterable<X> items, CollectionType results,
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
     * @return {@code results}
     */
    public static <X, CollectionType extends Collection<? super X>> CollectionType generate(int n, CollectionType results,
                                                                                            Function<Integer, X> generator)
    {
        return Iterate.each(Iterables.generate(n, generator)).visit(Iterate.collect(), results);
    }

    /**
     * Invokes {@code generator} {@code n} times, adding each result to {@code results}.
     *
     * @param n         number of times to invoke {@code generator}, and thus the number of items that will be added to
     *                  {@code results}.
     * @param generator {@link Function} to be invoked for each iteration
     * @return a new {@link List} containing {@code n} generated items.
     */
    public static <X> List<X> generate(int n, Function<Integer, X> generator)
    {
        return generate(n, new ArrayList<X>(n), generator);
    }

    public static <X, CollectionType extends Collection<? super X>> CollectionType generate(int n, CollectionType results, Provider<X> provider)
    {
        return Iterate.each(Iterables.generate(n, provider)).add(results);
    }

    public static <X> List<X> generate(int n, Provider<X> provider)
    {
        return generate(n, new ArrayList<X>(n), provider);
    }

    /**
     * Sorts {@code itemsToSort} based on the order of items in {@code exampleOrder}.  Any items in {@code itemsToSort} that
     * do not appear in {@code exampleOrder} <em>will not</em> be included in the returned results.
     *
     * @param exampleOrder example ordering by which {@code itemsToSort} will be sorted
     * @param itemsToSort  items to be sorted.  Note that unlike {@link Collections#sort(java.util.List)}, this method does not
     *                     modify {@code itemsToSort}.
     * @return a {@link List} sorted in the order provided in {@code exampleOrder}
     */
    public static <X> List<X> sortByExample(final Iterable<X> exampleOrder, final Iterable<X> itemsToSort)
    {
        return sortByExample(exampleOrder, itemsToSort, Functions.<X>identity());
    }

    /**
     * Sorts {@code itemsToSort} based on the order of items in {@code exampleOrder}.  Any items in {@code itemsToSort} that
     * do not appear in {@code exampleOrder} <em>will not</em> be included in the returned results.
     *
     * @param exampleOrder example ordering by which {@code itemsToSort} will be sorted
     * @param keyFunction  {@link Function} to provide a key by which items in {@code exampleOrder} and {@code itemsToSort} can be matched
     * @param itemsToSort  items to be sorted.  Note that unlike {@link Collections#sort(java.util.List)}, this method does not
     *                     modify {@code itemsToSort}.
     * @return a {@link List} sorted in the order provided in {@code exampleOrder}
     */
    public static <K, X> List<X> sortByExample(final Iterable<X> exampleOrder, final Function<? super X, K> keyFunction, final Iterable<X> itemsToSort)
    {
        return sortByExample(exampleOrder, keyFunction, itemsToSort, keyFunction);
    }

    /**
     * Sorts {@code itemsToSort} based on the order of items in {@code exampleOrder}.  Any items in {@code itemsToSort} that
     * do not appear in {@code exampleOrder} <em>will not</em> be included in the returned results.
     *
     * @param exampleOrder example ordering by which {@code itemsToSort} will be sorted
     * @param itemsToSort  items to be sorted.  Note that unlike {@link Collections#sort(java.util.List)}, this method does not
     *                     modify {@code itemsToSort}.
     * @param yKeyFunction  {@link Function} to provide a key by which items in {@code exampleOrder} and {@code itemsToSort} can be matched
     * @return a {@link List} sorted in the order provided in {@code exampleOrder}
     */
    public static <X, Y> List<Y> sortByExample(final Iterable<X> exampleOrder, final Iterable<Y> itemsToSort, final Function<? super Y, X> yKeyFunction)
    {
        return sortByExample(exampleOrder, Functions.<X>identity(), itemsToSort, yKeyFunction);
    }

    /**
     * Sorts {@code itemsToSort} based on the order of items in {@code exampleOrder}.  Any items in {@code itemsToSort} that
     * do not appear in {@code exampleOrder} <em>will not</em> be included in the returned results.
     *
     * @param exampleOrder example ordering by which {@code itemsToSort} will be sorted
     * @param xKeyFunction  {@link Function} to provide a key by which items in {@code exampleOrder} and {@code itemsToSort} can be matched
     * @param itemsToSort  items to be sorted.  Note that unlike {@link Collections#sort(java.util.List)}, this method does not
     *                     modify {@code itemsToSort}.
     * @param yKeyFunction  {@link Function} to provide a key by which items in {@code itemsToSort} and {@code itemsToSort} can be matched
     * @return a {@link List} sorted in the order provided in {@code exampleOrder}
     */
    public static <K, X, Y> List<Y> sortByExample(final Iterable<X> exampleOrder, final Function<? super X, K> xKeyFunction, final Iterable<Y> itemsToSort, final Function<? super Y, K> yKeyFunction)
    {
        // Use an inner join to correlate exampleOrder and itemsToSort to only items with appear in itemsToSort, but
        // preserving the order provided in exampleOrder.
        return Iterate.each(exampleOrder).join(Join.inner(xKeyFunction, yKeyFunction), itemsToSort)
                .map(Pair.<X, Y>y()).add(new ArrayList<Y>(Iterate.estimateSize(itemsToSort)));
    }

    private Algorithms()
    {
    }
}

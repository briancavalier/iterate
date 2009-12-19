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
package org.bc.iterate.iterable;

import org.bc.iterate.Function;
import org.bc.iterate.Iterate;
import org.bc.iterate.relational.JoinResult;

import java.util.*;

/**
 * Performs a full outer join, using a <a href="http://en.wikipedia.org/wiki/Hash_join">hash join</a>, yielding
 * the joined {@link org.bc.iterate.util.Pair}s as its iterator's items.  The join is done incrementally rather than as
 * a batch operation.
 *
 * @author Brian Cavalier
 */
public class FullIncrementalHashJoinIterable<K, X, Y> extends IncrementalJoinIterable<K, X, Y>
{
    private final Iterator<X> leftIterator;
    private final Function<? super X, K> xKeyFunction;
    private final Iterable<Y> rightIterable;
    private final Function<? super Y, K> yKeyFunction;
    private Map<K, List<Y>> rightMap;
    private Iterator<JoinResult<K, X, Y>> currentJoinIterator;
    private boolean left = true;
    private Iterator<Y> rightIterator;

    public FullIncrementalHashJoinIterable(Iterable<X> left,
                                           Function<? super X, K> xKeyFunction,
                                           Iterable<Y> right,
                                           Function<? super Y, K> yKeyFunction)
    {
        this.leftIterator = left.iterator();
        this.xKeyFunction = xKeyFunction;
        this.rightIterable = right;
        this.yKeyFunction = yKeyFunction;
    }

    @Override
    protected void prepareJoin()
    {
        final int rightSize = Iterate.estimateSize(rightIterable);
        rightMap = new HashMap<K, List<Y>>(rightSize);
        Set<Y> rightItems = new LinkedHashSet<Y>(rightSize);
        for (final Y item : rightIterable) {
            rightItems.add(item);
            put(rightMap, yKeyFunction.apply(item), item);
        }

        rightIterator = rightItems.iterator();
    }

    @Override
    protected JoinResult<K, X, Y> findNext()
    {
        if (left) {
            if (currentJoinIterator == null || !currentJoinIterator.hasNext()) {
                // First do left join until all left items exhausted
                final X item = leftIterator.next();
                final K key = xKeyFunction.apply(item);

                // Switch to right join mode if all left items have been joined.
                left = leftIterator.hasNext();

                List<Y> currentYList = rightMap.remove(key);
                if (currentYList != null) {
                    currentJoinIterator = buildLeftJoinIterator(key, item, currentYList);
                } else {
                    return new JoinResult<K, X, Y>(key, item, null);
                }
            }
            return currentJoinIterator != null && currentJoinIterator.hasNext() ? currentJoinIterator.next() : end();
        } else {
            // Do right join until all right items exhausted
            while (rightIterator.hasNext()) {
                final Y item = rightIterator.next();
                final K key = yKeyFunction.apply(item);
                if (rightMap.containsKey(key)) {
                    return new JoinResult<K, X, Y>(key, null, item);
                }
            }
            return end();
        }
    }

    private static <K, X, Y> Iterator<JoinResult<K, X, Y>> buildLeftJoinIterator(K key, X x, List<Y> yList)
    {
        final ArrayList<JoinResult<K, X, Y>> currentJoinList = new ArrayList<JoinResult<K, X, Y>>(yList.size());
        for (Y y : yList) {
            currentJoinList.add(new JoinResult<K, X, Y>(key, x, y));
        }
        return currentJoinList.iterator();
    }
}
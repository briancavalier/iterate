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
 * Performs a simple right outer join, using a <a href="http://en.wikipedia.org/wiki/Hash_join">hash join</a>, yielding
 * the joined {@link org.bc.iterate.util.Pair}s as its iterator's items.  The join is done incrementally rather than as
 * a batch operation.
 *
 * @param <X>
 * @param <K>
 * @param <Y>
 */
public class RightIncrementalHashJoinIterable<X, K, Y> extends IterableBase<JoinResult<K, X, Y>>
{
    private final Iterable<X> leftIterable;
    private final Function<? super X, K> xKeyFunction;
    private final Iterator<Y> rightIterator;
    private final Function<? super Y, K> yKeyFunction;
    private Map<K, List<X>> joinMap;
    private Iterator<JoinResult<K, X, Y>> currentJoinIterator;

    public RightIncrementalHashJoinIterable(Iterable<X> left,
                                           Function<? super X, K> xKeyFunction,
                                           Iterable<Y> right,
                                           Function<? super Y, K> yKeyFunction)
    {
        this.leftIterable = left;
        this.xKeyFunction = xKeyFunction;
        this.rightIterator = right.iterator();
        this.yKeyFunction = yKeyFunction;
    }

    @Override
    public Iterator<JoinResult<K, X, Y>> iterator()
    {
        prepareJoin(leftIterable, xKeyFunction);
        return super.iterator();
    }

    private void prepareJoin(Iterable<X> left, Function<? super X, K> xKeyFunction)
    {
        joinMap = new HashMap<K, List<X>>(Iterate.estimateSize(left));
        for (final X x : left) {
            //noinspection SuspiciousNameCombination
            K k = xKeyFunction.apply(x);
            List<X> xs = joinMap.get(k);
            if (xs == null) {
                xs = new ArrayList<X>(16);
                joinMap.put(k, xs);
            }
            xs.add(x);
        }
    }

    public boolean hasNext()
    {
        return rightIterator.hasNext() || (currentJoinIterator != null && currentJoinIterator.hasNext());
    }

    public JoinResult<K, X, Y> next()
    {
        if (currentJoinIterator == null || !currentJoinIterator.hasNext()) {
            final Y y = rightIterator.next();
            final K key = yKeyFunction.apply(y);
            List<X> currentXList = joinMap.get(key);
            if (currentXList != null) {
                final ArrayList<JoinResult<K, X, Y>> currentJoinList =
                        new ArrayList<JoinResult<K, X, Y>>(currentXList.size());
                for (X x : currentXList) {
                    currentJoinList.add(new JoinResult<K, X, Y>(key, x, y));
                }
                currentJoinIterator = currentJoinList.iterator();
            } else {
                currentJoinIterator = null;
                return new JoinResult<K, X, Y>(key, null, y);
            }
        }

        return currentJoinIterator.next();
    }
}
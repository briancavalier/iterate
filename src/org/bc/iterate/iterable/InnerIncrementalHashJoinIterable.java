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
 * Performs a simple left outer join, using a <a href="http://en.wikipedia.org/wiki/Hash_join">hash join</a>, yielding
 * the joined {@link org.bc.iterate.util.Pair}s as its iterator's items.  The join is done incrementally rather than as
 * a batch operation.
 *
 * @param <X>
 * @param <K>
 * @param <Y>
 */
public class InnerIncrementalHashJoinIterable<X, K, Y> extends LookaheadIterable<JoinResult<K, X, Y>>
{
    private final Iterator<X> leftIterator;
    private final Function<? super X, K> xKeyFunction;
    private final Iterable<Y> rightIterable;
    private final Function<? super Y, K> yKeyFunction;
    private Map<K, List<Y>> joinMap;
    private Iterator<JoinResult<K, X, Y>> currentJoinIterator;

    public InnerIncrementalHashJoinIterable(Iterable<X> left,
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
    public Iterator<JoinResult<K, X, Y>> iterator()
    {
        prepareJoin(rightIterable, yKeyFunction);
        return super.iterator();
    }

    private void prepareJoin(Iterable<Y> right, Function<? super Y, K> yKeyFunction)
    {
        joinMap = new HashMap<K, List<Y>>(Iterate.estimateSize(right));
        for (final Y y : right) {
            //noinspection SuspiciousNameCombination
            K k = yKeyFunction.apply(y);
            List<Y> ys = joinMap.get(k);
            if (ys == null) {
                ys = new ArrayList<Y>(16);
                joinMap.put(k, ys);
            }
            ys.add(y);
        }
    }

    @Override
    protected JoinResult<K, X, Y> findNext()
    {
        if (currentJoinIterator == null || !currentJoinIterator.hasNext()) {
            boolean joined = false;
            while(leftIterator.hasNext() && !joined) {
                final X x = leftIterator.next();
                final K key = xKeyFunction.apply(x);
                List<Y> currentYList = joinMap.get(key);
                if (currentYList != null) {
                    final ArrayList<JoinResult<K, X, Y>> currentJoinList =
                            new ArrayList<JoinResult<K, X, Y>>(currentYList.size());
                    for (Y y : currentYList) {
                        currentJoinList.add(new JoinResult<K, X, Y>(key, x, y));
                    }
                    currentJoinIterator = currentJoinList.iterator();
                    joined = true;
                } else {
                    currentJoinIterator = null;
                }
            }
        }

        return currentJoinIterator != null && currentJoinIterator.hasNext() ? currentJoinIterator.next() : end();
    }
}
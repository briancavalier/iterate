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

import org.bc.iterate.Function;
import org.bc.iterate.Iterate;
import org.bc.iterate.relational.JoinResult;

import java.util.*;

/**
 * Performs a right outer join, using a <a href="http://en.wikipedia.org/wiki/Hash_join">hash join</a>, yielding
 * the joined {@link org.bc.iterate.util.Pair}s as its iterator's items.  The join is done incrementally rather than as
 * a batch operation.
 *
 * @author Brian Cavalier
 */
public class RightIncrementalHashJoinIterable<K, X, Y> extends IncrementalJoinIterable<K, X, Y>
{
    private final Iterable<X> leftIterable;
    private final Function<? super X, K> xKeyFunction;
    private final Iterable<Y> rightIterable;
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
        this.rightIterable = right;
        this.yKeyFunction = yKeyFunction;
    }

    @Override
    protected IncrementalJoinIterator createIterator()
    {
        return new RightIncrementalHashJoinIterator();
    }

    private class RightIncrementalHashJoinIterator extends IncrementalJoinIterator
    {
        private final Iterator<Y> rightIterator = rightIterable.iterator();
        @Override
        protected void prepareJoin()
        {
            joinMap = new HashMap<K, List<X>>(Iterate.estimateSize(leftIterable));
            for (final X item : leftIterable) {
                put(joinMap, xKeyFunction.apply(item), item);
            }
        }

        @Override
        protected JoinResult<K, X, Y> findNext()
        {
            if (currentJoinIterator == null || (!currentJoinIterator.hasNext() && rightIterator.hasNext())) {
                final Y item = rightIterator.next();
                final K key = yKeyFunction.apply(item);
                List<X> currentXList = joinMap.get(key);
                if (currentXList != null) {
                    final ArrayList<JoinResult<K, X, Y>> currentJoinList =
                            new ArrayList<JoinResult<K, X, Y>>(currentXList.size());
                    for (X x : currentXList) {
                        currentJoinList.add(new JoinResult<K, X, Y>(key, x, item));
                    }
                    currentJoinIterator = currentJoinList.iterator();
                } else {
                    return new JoinResult<K, X, Y>(key, null, item);
                }
            }

            return currentJoinIterator != null && currentJoinIterator.hasNext() ? currentJoinIterator.next() : end();
        }
    }
}
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

package org.bc.iterate.relational;

import org.bc.iterate.Function;
import org.bc.iterate.iterable.InnerIncrementalHashJoinIterable;
import org.bc.iterate.iterable.LeftIncrementalHashJoinIterable;
import org.bc.iterate.iterable.RightIncrementalHashJoinIterable;

/**
 * Factory methods for easy creation of {@link JoinStrategy} instances for use with
 * {@link org.bc.iterate.Iterate#join(JoinStrategy, Iterable)}.
 * 
 * @author Brian Cavalier
 */
public class Join
{
    public static <K, X, Y> JoinStrategy<K, X, Y> left(final Function<? super X, K> xKeyFunction, final Function<? super Y, K> yKeyFunction)
    {
        return new JoinStrategy<K, X, Y>()
        {
            public Iterable<JoinResult<K, X, Y>> join(final Iterable<X> left, final Iterable<Y> right)
            {
                return new LeftIncrementalHashJoinIterable<K, X, Y>(left, xKeyFunction, right, yKeyFunction);
            }
        };
    }

    public static <K, X> JoinStrategy<K, X, X> left(final Function<? super X, K> keyFunction)
    {
        return left(keyFunction, keyFunction);
    }

    public static <K, X, Y> JoinStrategy<K, X, Y> right(final Function<? super X, K> xKeyFunction, final Function<? super Y, K> yKeyFunction)
    {
        return new JoinStrategy<K, X, Y>()
        {
            public Iterable<JoinResult<K, X, Y>> join(final Iterable<X> left, final Iterable<Y> right)
            {
                return new RightIncrementalHashJoinIterable<K, X, Y>(left, xKeyFunction, right, yKeyFunction);
            }
        };
    }

    public static <K, X> JoinStrategy<K, X, X> right(final Function<? super X, K> keyFunction)
    {
        return right(keyFunction, keyFunction);
    }

    public static <K, X, Y> JoinStrategy<K, X, Y> inner(final Function<? super X, K> xKeyFunction, final Function<? super Y, K> yKeyFunction)
    {
        return new JoinStrategy<K, X, Y>()
        {
            public Iterable<JoinResult<K, X, Y>> join(final Iterable<X> left, final Iterable<Y> right)
            {
                return new InnerIncrementalHashJoinIterable<K, X, Y>(left, xKeyFunction, right, yKeyFunction);
            }
        };
    }

    public static <K, X> JoinStrategy<K, X, X> inner(final Function<? super X, K> keyFunction)
    {
        return inner(keyFunction, keyFunction);
    }
}

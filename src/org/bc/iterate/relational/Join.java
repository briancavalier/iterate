package org.bc.iterate.relational;

import org.bc.iterate.BinaryFunction;
import org.bc.iterate.Function;
import org.bc.iterate.iterable.InnerIncrementalHashJoinIterable;
import org.bc.iterate.iterable.LeftIncrementalHashJoinIterable;
import org.bc.iterate.iterable.RightIncrementalHashJoinIterable;
import org.bc.iterate.util.Pair;

/**
 * @author bcavalier
 */
public class Join
{    
    public static <K, X, Y> JoinStrategy<K, X, Y> left(final Function<? super X, K> xKeyFunction, final Function<? super Y, K> yKeyFunction)
    {
        return new JoinStrategy<K, X, Y>()
        {
            public Iterable<JoinResult<K, X, Y>> join(final Iterable<X> left, final Iterable<Y> right)
            {
                return new LeftIncrementalHashJoinIterable<X, K ,Y>(left, xKeyFunction, right, yKeyFunction);
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
                return new RightIncrementalHashJoinIterable<X, K ,Y>(left, xKeyFunction, right, yKeyFunction);
            }
        };
    }

    public static <K, X> JoinStrategy<K, X, X> right(final Function<?super X, K> keyFunction)
    {
        return right(keyFunction, keyFunction);
    }

    public static <K, X, Y> JoinStrategy<K, X, Y> inner(final Function<? super X, K> xKeyFunction, final Function<? super Y, K> yKeyFunction)
    {
        return new JoinStrategy<K, X, Y>()
        {
            public Iterable<JoinResult<K, X, Y>> join(final Iterable<X> left, final Iterable<Y> right)
            {
                return new InnerIncrementalHashJoinIterable<X, K ,Y>(left, xKeyFunction, right, yKeyFunction);
            }
        };
    }

    public static <K, X> JoinStrategy<K, X, X> inner(final Function<?super X, K> keyFunction)
    {
        return right(keyFunction, keyFunction);
    }
}

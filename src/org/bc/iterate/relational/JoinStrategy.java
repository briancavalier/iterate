package org.bc.iterate.relational;

import org.bc.iterate.relational.JoinResult;

/**
 * @author bcavalier
 */
public interface JoinStrategy<K, X, Y>
{
    Iterable<JoinResult<K, X, Y>> join(Iterable<X> left, Iterable<Y> right);
}

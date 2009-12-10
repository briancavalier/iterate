package org.bc.iterate.iterable;

import org.bc.iterate.relational.JoinResult;

import java.util.Iterator;

/**
 * Base template class for incremental join implementations.
 * 
 * @author Brian Cavalier
 */
public abstract class IncrementalJoinIterable<K, X, Y> extends LookaheadIterable<JoinResult<K, X, Y>>
{
    @Override
    public Iterator<JoinResult<K, X, Y>> iterator()
    {
        prepareJoin();
        return super.iterator();
    }

    /**
     * Subclasses should implement this to initialize any internal data structures required by their
     * implementation of {@link #findNext()}. Called by @{link #iterator()} 
     */
    protected abstract void prepareJoin();
}

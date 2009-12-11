package org.bc.iterate.iterable;

import org.bc.iterate.relational.JoinResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    protected static <K, T> void put(Map<K, List<T>> multimap, K key, T x)
    {
        List<T> items = multimap.get(key);
        if(items == null) {
            items = new ArrayList<T>(16);
            multimap.put(key, items);
        }
        items.add(x);
    }
}

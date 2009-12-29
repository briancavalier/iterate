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

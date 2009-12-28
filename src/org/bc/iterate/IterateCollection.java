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
package org.bc.iterate;

import java.util.Collection;

public class IterateCollection<X, Y extends Collection<X>> extends Iterate<X> implements Collection<X>
{
    private final Y wrapped;

    @SuppressWarnings({"AssignmentToCollectionOrArrayFieldFromParameter"})
    public IterateCollection(Y wrapped)
    {
        super(wrapped, wrapped.size());
        this.wrapped = wrapped;
    }

    public Y unwrap()
    {
        return wrapped;
    }

    public int size()
    {
        return wrapped.size();
    }

    public boolean isEmpty()
    {
        return wrapped.isEmpty();
    }

    public boolean contains(Object o)
    {
        return wrapped.contains(o);
    }

    public Object[] toArray()
    {
        return wrapped.toArray();
    }

    @SuppressWarnings({"SuspiciousToArrayCall"})
    public <T> T[] toArray(T[] ts)
    {
        return wrapped.toArray(ts);
    }

    public boolean add(X x)
    {
        return wrapped.add(x);
    }

    public boolean remove(Object o)
    {
        return wrapped.remove(o);
    }

    public boolean containsAll(Collection<?> objects)
    {
        return wrapped.containsAll(objects);
    }

    public boolean addAll(Collection<? extends X> xes)
    {
        return wrapped.addAll(xes);
    }

    public boolean removeAll(Collection<?> objects)
    {
        return wrapped.removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects)
    {
        return wrapped.retainAll(objects);
    }

    public void clear()
    {
        wrapped.clear();
    }
}

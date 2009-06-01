/*
 * Copyright 2007-2009 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bc.iterate.iterable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GroupIterable<X> extends LookaheadIterable<Collection<X>>
{
    private final Iterator<X> iterator;
    private final int groupSize;

    public GroupIterable(Iterable<X> iterable, int groupSize)
    {
        this.iterator = iterable.iterator();
        this.groupSize = groupSize;
    }

    @Override
    protected Collection<X> findNext()
    {
        if (!iterator.hasNext()) {
            return null;
        }

        List<X> nextGroup = new ArrayList<X>(groupSize);
        int i = 0;
        while (i < groupSize && iterator.hasNext()) {
            nextGroup.add(iterator.next());
            i++;
        }

        return nextGroup;
    }
}

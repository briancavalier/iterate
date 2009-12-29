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

import java.util.Arrays;
import java.util.Iterator;

/**
 * An {@link Iterable} over items in each of the supplied {@link Iterable}s, which return items from the
 * first {@link Iterable} until exhausted followed by items from the second {@link Iterable} until exhausted, and
 * so on.
 *
 * @author Brian Cavalier
 */
public class ConcatIterable<X> extends IterableBase<X>
{
    final private Iterator<? extends Iterable<X>> nested;
    private Iterator<X> current;

    public ConcatIterable(Iterable<? extends Iterable<X>> nested)
    {
        this.nested = nested.iterator();
        this.current = this.nested.next().iterator();
    }

    public ConcatIterable(Iterable<X>... nested)
    {
        this(Arrays.asList(nested));
    }

    public boolean hasNext()
    {
        if (current.hasNext()) {
            return true;
        }

        if(nested.hasNext()) {
            current = nested.next().iterator();
            while(!current.hasNext() && nested.hasNext()) {
                current = nested.next().iterator();
            }

            return current.hasNext();
        }

        return false;
    }

    public X next()
    {
        return current.next();
    }

    @SuppressWarnings({"RefusedBequest"})
    @Override
    public void remove()
    {
        current.remove();
    }
}

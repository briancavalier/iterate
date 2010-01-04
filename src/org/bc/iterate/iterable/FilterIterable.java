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

import org.bc.iterate.Condition;

import java.util.Iterator;

public class FilterIterable<X> implements Iterable<X>
{
    protected final Condition<? super X> filter;
    protected final Iterable<X> iterable;

    public FilterIterable(Iterable<X> iterable, Condition<? super X> filter)
    {
        this.filter = filter;
        this.iterable = iterable;
    }

    @Override
    public Iterator<X> iterator()
    {
        return new FilterIterator();
    }

    @Override
    public String toString()
    {
        return iterable.toString() + ' ' + filter;
    }

    protected class FilterIterator extends LookaheadIterator<X>
    {
        protected final Iterator<X> iterator = (Iterator<X>) iterable.iterator();

        @Override
        protected X findNext()
        {
            while (iterator.hasNext()) {
                X item = iterator.next();
                if (filter.eval(item)) {
                    return item;
                }
            }

            return end();
        }

        @SuppressWarnings({"RefusedBequest"})
        public void remove()
        {
            iterator.remove();
        }
    }
}

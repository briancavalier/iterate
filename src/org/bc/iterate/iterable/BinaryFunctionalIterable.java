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

import org.bc.iterate.BinaryFunction;

import java.util.Iterator;

public class BinaryFunctionalIterable<X, Y, Z> implements Iterable<Y>
{
    private final Iterable<X> iterable;

    private Z param;

    private final BinaryFunction<? super X, ? super Z, Y> f;

    public BinaryFunctionalIterable(Iterable<X> xItems, Z param, BinaryFunction<? super X, ? super Z, Y> f)
    {
        this.iterable = xItems;
        this.f = f;
        this.param = param;
    }

    @Override
    public Iterator<Y> iterator()
    {
        return new BinaryFunctionalIterator();
    }

    public String toString()
    {
        return f.toString() + '(' + iterable + ", " + param + ')';
    }

    private class BinaryFunctionalIterator extends AbstractIterator<Y>
    {
        private final Iterator<X> iterator = iterable.iterator();

        public boolean hasNext()
        {
            return iterator.hasNext();
        }

        public Y next()
        {
            return f.apply(iterator.next(), param);
        }
    }
}

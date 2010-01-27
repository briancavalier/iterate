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

import org.bc.iterate.Function;
import org.bc.iterate.Iterate;

import java.util.Iterator;

/**
 * {@link Iterable} that produces a specified number items using a supplied {@link Function}.
 *  
 * @author Brian Cavalier
 */
public class GeneratorIterable<X> implements Iterable<X>
{
    private final int end;
    private final Function<Integer, X> generator;

    public GeneratorIterable(int n, Function<Integer, X> generator)
    {
        this.end = n;
        this.generator = generator;
    }

    @Override
    public Iterator<X> iterator()
    {
        return new GeneratorIterator();
    }

    private class GeneratorIterator extends AbstractIterator<X>
    {
        private int index = 0;

        public boolean hasNext()
        {
            return index < end;
        }

        @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
        public X next()
        {
            return generator.apply(index++);
        }
    }
}

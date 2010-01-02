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

import org.bc.iterate.iterable.IterableBase;

import java.util.Random;

/**
 * This class provides {@link Function}s and {@link Visitor}s for {@code long}s and {@link Long}s.
 *
 * @author Brian Cavalier
 */
public class Longs
{
    public static Function<Long, Long> identity()
    {
        return Functions.identity();
    }

    public static Function<String, Long> parse()
    {
        return new Function<String, Long>()
        {
            public Long apply(String s)
            {
                return Long.valueOf(s);
            }
        };
    }

    public static BinaryFunction<Long, Long, Long> sum()
    {
        return new BinaryFunction<Long, Long, Long>()
        {
            public Long apply(Long i1, Long i2)
            {
                return i1 + i2;
            }
        };
    }

    public static BinaryFunction<Long, Long, Long> multiply()
    {
        return new BinaryFunction<Long, Long, Long>()
        {
            public Long apply(Long i1, Long i2)
            {
                return i1 * i2;
            }
        };
    }

    public static BinaryFunction<Long, Double, Double> pow()
    {
        return new BinaryFunction<Long, Double, Double>()
        {
            public Double apply(Long i, Double exponent)
            {
                return Math.pow(i, exponent);
            }
        };
    }

    public static Monoid<Long> square()
    {
        return new Monoid<Long>()
        {
            public Long apply(Long i)
            {
                return i * i;
            }
        };
    }

    public static Monoid<Long> cube()
    {
        return new Monoid<Long>()
        {
            public Long apply(Long i)
            {
                return i * i * i;
            }
        };
    }

    /**
     * @return a inexhaustable {@link Iterable} of random {@link Long}s. See {@link java.util.Random#nextLong()}
     *         for numeric range of returned values.
     */
    public static Iterable<Long> random()
    {
        final Random random = new Random();
        return new IterableBase<Long>()
        {
            public boolean hasNext()
            {
                return true;
            }

            @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
            public Long next()
            {
                return random.nextLong();
            }
        };
    }

    /**
     * @param random {@link Random} to use to generate random {@link Float}s
     * @return a inexhaustable {@link Iterable} of random {@link Long}s using the supplied {@link Random}.
     *         See {@link java.util.Random#nextLong()} for numeric range of returned values.
     */
    public static Iterable<Long> random(final Random random)
    {
        return new IterableBase<Long>()
        {
            public boolean hasNext()
            {
                return true;
            }

            @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
            public Long next()
            {
                return random.nextLong();
            }
        };
    }

}
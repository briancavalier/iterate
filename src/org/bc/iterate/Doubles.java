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

import org.bc.iterate.function.Identity;
import org.bc.iterate.iterable.IterableBase;

import java.util.Random;

/**
 * This class provides {@link Function}s and {@link Visitor}s for {@code double}s and {@link Double}s.
 *
 * @author Brian Cavalier
 */
public class Doubles
{
    public static Function<Double, Double> identity()
    {
        return new Identity<Double>();
    }

    public static Function<String, Double> parse()
    {
        return new Function<String, Double>()
        {
            public Double apply(String s)
            {
                return Double.valueOf(s);
            }
        };
    }

    public static BinaryFunction<Double, Double, Double> sum()
    {
        return new BinaryFunction<Double, Double, Double>()
        {
            public Double apply(Double i1, Double i2)
            {
                return i1 + i2;
            }
        };
    }

    public static BinaryFunction<Double, Double, Double> multiply()
    {
        return new BinaryFunction<Double, Double, Double>()
        {
            public Double apply(Double i1, Double i2)
            {
                return i1 * i2;
            }
        };
    }

    public static BinaryFunction<Double, Double, Double> pow()
    {
        return new BinaryFunction<Double, Double, Double>()
        {
            public Double apply(Double i, Double exponent)
            {
                return Math.pow(i, exponent);
            }
        };
    }

    public static Monoid<Double> square()
    {
        return new Monoid<Double>()
        {
            public Double apply(Double i)
            {
                return i * i;
            }
        };
    }

    public static Monoid<Double> cube()
    {
        return new Monoid<Double>()
        {
            public Double apply(Double i)
            {
                return i * i * i;
            }
        };
    }

    /**
     * @return a inexhaustable {@link Iterable} of random {@link Double}s. See {@link java.util.Random#nextDouble()}
     * for numeric range of returned values.
     */
    public static Iterable<Double> random()
    {
        final Random random = new Random();
        return new IterableBase<Double>()
        {
            public boolean hasNext()
            {
                return true;
            }

            @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
            public Double next()
            {
                return random.nextDouble();
            }
        };
    }

    /**
     * @param random {@link Random} to use to generate random {@link Double}s
     * @return a inexhaustable {@link Iterable} of random {@link Double}s using the supplied {@link Random}.
     * See {@link java.util.Random#nextDouble()} for numeric range of returned values.
     */
    public static Iterable<Double> random(final Random random)
    {
        return new IterableBase<Double>()
        {
            public boolean hasNext()
            {
                return true;
            }

            @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
            public Double next()
            {
                return random.nextDouble();
            }
        };
    }

}
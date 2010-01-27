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

import org.bc.iterate.iterable.AbstractIterator;
import org.bc.iterate.iterable.IntegerRange;

import java.util.Iterator;
import java.util.Random;

/**
 * This class provides {@link Function}s and {@link Visitor}s for {@code int}s and {@link Integer}s.
 *
 * @author Brian Cavalier
 */
public class Integers
{
    /**
     * @return the identity function for integers.
     */
    public static Function<Integer, Integer> identity()
    {
        return Functions.identity();
    }

    /**
     * @return a {@link Function} that will parse a {@link String} into an {@link Integer}
     */
    public static Function<String, Integer> parse()
    {
        return new Function<String, Integer>()
        {
            public Integer apply(String s)
            {
                return Integer.valueOf(s);
            }
        };
    }

    /**
     * @return a {@link BinaryFunction} that returns the result of adding its two arguments, i.e. {@code x + y}
     */
    public static BinaryFunction<Integer, Integer, Integer> sum()
    {
        return new BinaryFunction<Integer, Integer, Integer>()
        {
            public Integer apply(Integer i1, Integer i2)
            {
                return i1 + i2;
            }

            public String toString()
            {
                return "+";
            }
        };
    }

    /**
     * a {@link BinaryFunction} that returns the result of multiplying its two arguments, i.e. {@code x * y}
     *
     * @return a {@link BinaryFunction} that returns the result of multiplying its two arguments, i.e. {@code x * y}
     */
    public static BinaryFunction<Integer, Integer, Integer> multiply()
    {
        return new BinaryFunction<Integer, Integer, Integer>()
        {
            public Integer apply(Integer i1, Integer i2)
            {
                return i1 * i2;
            }

            public String toString()
            {
                return "*";
            }
        };
    }

    /**
     * @return a {@link BinaryFunction} that returns the result of {@code Math.pow(x,y)} as a {@link Double} value.
     */
    public static BinaryFunction<Integer, Double, Double> pow()
    {
        return new BinaryFunction<Integer, Double, Double>()
        {
            public Double apply(Integer i, Double exponent)
            {
                return Math.pow(i, exponent);
            }
        };
    }

    /**
     * @return a {@link Function} that returns the result of squaring its argument, i.e. {@code x * x}
     */
    public static Monoid<Integer> square()
    {
        return new Monoid<Integer>()
        {
            public Integer apply(Integer i)
            {
                return i * i;
            }
        };
    }

    /**
     * @return a {@link Function} that returns the result of cubing its argument, i.e. {@code x * x * x}
     */
    public static Monoid<Integer> cube()
    {
        return new Monoid<Integer>()
        {
            public Integer apply(Integer i)
            {
                return i * i * i;
            }
        };
    }

    /**
     * @param maxValueExclusive random integers strictly less than this value will be returned by the {@link Iterable}
     *
     * @return a inexhaustable {@link Iterable} of random integers from {@code 0} to {@code maxValueExclusive}
     */
    public static Iterable<Integer> random(final int maxValueExclusive)
    {
        return random(new Random(), maxValueExclusive);
    }

    /**
     * @param random            {@link Random} to use to generate random integers.
     * @param maxValueExclusive random integers strictly less than this value will be returned by the {@link Iterable}
     *
     * @return a inexhaustable {@link Iterable} of random integers from {@code 0} to {@code maxValueExclusive} using the
     *         supplied {@link Random}
     */
    public static Iterate<Integer> random(final Random random, final int maxValueExclusive)
    {
        return Iterate.each(new Iterable<Integer>()
        {
            @Override
            public Iterator<Integer> iterator()
            {
                return new AbstractIterator<Integer>()
                {
                    public boolean hasNext()
                    {
                        return true;
                    }

                    @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
                    public Integer next()
                    {
                        return random.nextInt(maxValueExclusive);
                    }
                };
            }
        });
    }

    /**
     * @return a inexhaustable {@link Iterable} of random integers from {@code 0} to {@link Integer#MAX_VALUE}
     */
    public static Iterable<Integer> random()
    {
        return random(new Random());
    }

    /**
     * @param random {@link Random} to use to generate random integers.
     *
     * @return a inexhaustable {@link Iterable} of random integers from {@code 0} to {@link Integer#MAX_VALUE} using the
     *         supplied {@link Random}.
     */
    public static Iterate<Integer> random(final Random random)
    {
        return Iterate.each(new Iterable<Integer>()
        {
            @Override
            public Iterator<Integer> iterator()
            {
                return new AbstractIterator<Integer>()
                {
                    public boolean hasNext()
                    {
                        return true;
                    }

                    @SuppressWarnings({"IteratorNextCanNotThrowNoSuchElementException"})
                    public Integer next()
                    {
                        return random.nextInt();
                    }
                };
            }
        });
    }

    /**
     * @param start first integer value
     * @param end   exclusive last integer value
     *
     * @return an {@link Iterable} which will iterate over the integers from {@code start} (inclusive) to {@code end}
     *         (exclusive)
     */
    public static IntegerRange range(int start, int end)
    {
        return new IntegerRange(start, end);
    }

    /**
     * @param end exclusive last integer value
     *
     * @return an {@link Iterable} which will iterate over the integers from {@code 0} (zero, inclusive) to {@code end}
     *         (exclusive)
     */
    public static IntegerRange upto(int end)
    {
        return new IntegerRange(0, end);
    }
}

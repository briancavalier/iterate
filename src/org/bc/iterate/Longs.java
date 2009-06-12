/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.Identity;

/**
 * This class provides {@link Function}s and {@link Visitor}s for {@code long}s and {@link Long}s.
 *
 * @author Brian Cavalier
 */
public class Longs
{
    public static Function<Long, Long> identity()
    {
        return new Identity<Long>();
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
}
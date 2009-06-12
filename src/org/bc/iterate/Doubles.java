/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.Identity;

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
}
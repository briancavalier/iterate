/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.Identity;

/**
 * This class provides {@link Function}s, {@link Visitor}s, etc. for {@code float}s and {@link Float}s.
 *
 * @author Brian Cavalier
 */
public class Floats
{
    public static Function<Float, Float> identity()
    {
        return new Identity<Float>();
    }

    public static Function<String, Float> parse()
    {
        return new Function<String, Float>()
        {
            public Float apply(String s)
            {
                return Float.valueOf(s);
            }
        };
    }

    public static BinaryFunction<Float, Float, Float> sum()
    {
        return new BinaryFunction<Float, Float, Float>()
        {
            public Float apply(Float i1, Float i2)
            {
                return i1 + i2;
            }
        };
    }

    public static BinaryFunction<Float, Float, Float> multiply()
    {
        return new BinaryFunction<Float, Float, Float>()
        {
            public Float apply(Float i1, Float i2)
            {
                return i1 * i2;
            }
        };
    }

    public static BinaryFunction<Float, Double, Double> pow()
    {
        return new BinaryFunction<Float, Double, Double>()
        {
            public Double apply(Float i, Double exponent)
            {
                return Math.pow(i, exponent);
            }
        };
    }

    public static Monoid<Float> square()
    {
        return new Monoid<Float>()
        {
            public Float apply(Float i)
            {
                return i * i;
            }
        };
    }

    public static Monoid<Float> cube()
    {
        return new Monoid<Float>()
        {
            public Float apply(Float i)
            {
                return i * i * i;
            }
        };
    }
}
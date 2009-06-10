/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.Identity;

public class Integers
{
    public static Function<Integer, Integer> identity()
    {
        return new Identity<Integer>();
    }

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

    public static BinaryFunction<Integer, Integer, Integer> sum()
    {
        return new BinaryFunction<Integer, Integer, Integer>()
        {
            public Integer apply(Integer i1, Integer i2)
            {
                return i1 + i2;
            }
        };
    }

    public static BinaryFunction<Integer, Integer, Integer> multiply()
    {
        return new BinaryFunction<Integer, Integer, Integer>()
        {
            public Integer apply(Integer i1, Integer i2)
            {
                return i1 * i2;
            }
        };
    }

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
}

/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.examples;

import static org.bc.iterate.Functions.compose;
import static org.bc.iterate.Integers.square;
import static org.bc.iterate.Integers.sum;
import static org.bc.iterate.Iterate.each;
import static org.bc.iterate.Iterate.range;

public class SumOfSquares
{
    public static void main(String[] args)
    {
        // These are all equivalent ways of computing sum of squares.
        // Each will print the same result, the sum of squared integers from 1 to 100

        System.out.println(each(range(1, 101))
                .map(square())
                .reduce(sum(), 0));

        System.out.println(each(range(1, 101))
                .map(square())
                .reduce(sum()));

        // Looking more LISP-like ...
        System.out.println(each(range(1, 101))
                .reduce(compose(square(), sum()), 0));

        System.out.println(each(range(1, 101))
                .reduce(compose(square(), sum())));

    }
}

/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.examples;

import static org.bc.iterate.Integers.square;
import static org.bc.iterate.Integers.sum;
import org.bc.iterate.Iterate;
import static org.bc.iterate.Iterate.each;

public class SumOfSquares
{
    public static void main(String[] args)
    {
        System.out.println(each(Iterate.range(1, 101))
                .map(square())
                .reduce(sum()));
    }
}

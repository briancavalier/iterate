/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.junit.Test;
import static org.junit.Assert.*;

public class DoublesTest
{
    @Test
    public void identity()
    {
        assertEquals(Double.valueOf(1.0), Doubles.identity().apply(1.0));
    }

    @Test
    public void parse()
    {
        assertEquals(Double.valueOf(1.0), Doubles.parse().apply("1"));
        assertEquals(Double.valueOf(12.34), Doubles.parse().apply("12.34"));
        assertEquals(Double.valueOf(-12.34), Doubles.parse().apply("-12.34"));
    }

    @Test
    public void sum()
    {
        assertEquals(Double.valueOf(3.5), Doubles.sum().apply(1.25, 2.25));
    }

    @Test
    public void multiply()
    {
        assertEquals(Double.valueOf(1.2), Doubles.multiply().apply(0.3, 4.0));
    }

    @Test
    public void pow()
    {
        assertEquals(Double.valueOf(Math.pow(2.5, 1.5)), Doubles.pow().apply(2.5, 1.5));
    }

    @Test
    public void square()
    {
        assertEquals(Double.valueOf(4.3*4.3), Doubles.square().apply(4.3));
    }

    @Test
    public void cube()
    {
        assertEquals(Double.valueOf(5.6*5.6*5.6), Doubles.cube().apply(5.6));
    }
}
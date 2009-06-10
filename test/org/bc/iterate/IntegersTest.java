/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntegersTest
{
    @Test
    public void identity()
    {
        assertEquals(Integer.valueOf(1), Integers.identity().apply(1));
    }

    @Test
    public void parse()
    {
        assertEquals(Integer.valueOf(1), Integers.parse().apply("1"));
        assertEquals(Integer.valueOf(1234), Integers.parse().apply("1234"));
        assertEquals(Integer.valueOf(-1234), Integers.parse().apply("-1234"));
    }

    @Test
    public void sum()
    {
        assertEquals(Integer.valueOf(3), Integers.sum().apply(1, 2));
    }

    @Test
    public void multiply()
    {
        assertEquals(Integer.valueOf(12), Integers.multiply().apply(3, 4));
    }

    @Test
    public void pow()
    {
        assertEquals(Double.valueOf(8.0), Integers.pow().apply(2, 3.0));
    }

    @Test
    public void square()
    {
        assertEquals(Integer.valueOf(16), Integers.square().apply(4));
    }

    @Test
    public void cube()
    {
        assertEquals(Integer.valueOf(64), Integers.cube().apply(4));
    }
}

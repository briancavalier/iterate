/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.junit.Test;
import static org.junit.Assert.*;

public class FloatsTest
{
    @Test
    public void identity()
    {
        assertEquals(Float.valueOf(1.0f), Floats.identity().apply(1.0f));
    }

    @Test
    public void parse()
    {
        assertEquals(Float.valueOf(1.0f), Floats.parse().apply("1"));
        assertEquals(Float.valueOf(12.34f), Floats.parse().apply("12.34"));
        assertEquals(Float.valueOf(-12.34f), Floats.parse().apply("-12.34"));
    }

    @Test
    public void sum()
    {
        assertEquals(Float.valueOf(3.5f), Floats.sum().apply(1.25f, 2.25f));
    }

    @Test
    public void multiply()
    {
        assertEquals(Float.valueOf(1.2f), Floats.multiply().apply(0.3f, 4.0f));
    }

    @Test
    public void pow()
    {
        assertEquals(Double.valueOf(Math.pow(2.5, 1.5)), Floats.pow().apply(2.5f, 1.5));
    }

    @Test
    public void square()
    {
        assertEquals(Float.valueOf(4.3f*4.3f), Floats.square().apply(4.3f));
    }

    @Test
    public void cube()
    {
        assertEquals(Float.valueOf(5.6f*5.6f*5.6f), Floats.cube().apply(5.6f));
    }
}
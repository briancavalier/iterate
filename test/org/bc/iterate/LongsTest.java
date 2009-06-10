/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.junit.Test;
import static org.junit.Assert.*;

public class LongsTest
{
    @Test
    public void identity()
    {
        assertEquals(Long.valueOf(1L), Longs.identity().apply(1L));
    }

    @Test
    public void parse()
    {
        assertEquals(Long.valueOf(1L), Longs.parse().apply("1"));
        assertEquals(Long.valueOf(1234L), Longs.parse().apply("1234"));
        assertEquals(Long.valueOf(-1234L), Longs.parse().apply("-1234"));
    }

    @Test
    public void sum()
    {
        assertEquals(Long.valueOf(3L), Longs.sum().apply(1L, 2L));
    }

    @Test
    public void multiply()
    {
        assertEquals(Long.valueOf(12L), Longs.multiply().apply(3L, 4L));
    }

    @Test
    public void pow()
    {
        assertEquals(Double.valueOf(8.0), Longs.pow().apply(2L, 3.0));
    }

    @Test
    public void square()
    {
        assertEquals(Long.valueOf(16L), Longs.square().apply(4L));
    }

    @Test
    public void cube()
    {
        assertEquals(Long.valueOf(64L), Longs.cube().apply(4L));
    }
}
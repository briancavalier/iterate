package org.bc.iterate.iterable;

import org.bc.iterate.Functions;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author bcavalier
 */
public class GeneratorIterableTest
{
    @Test
    public void next() throws Exception
    {
        int count = 0;
        for (Integer i : new GeneratorIterable<Integer>(10, Functions.<Integer>identity())) {
            count++;
        }

        Assert.assertEquals(10, count);
    }
}

/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.Function;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompositeFunctionTest
{
    @Test
    public void apply() throws Exception
    {
        final CompositeFunction<String, Integer, String> gOfF =
                new CompositeFunction<String, Integer, String>(new Function<String, Integer>()
                {
                    public Integer apply(String s)
                    {
                        return Integer.parseInt(s);
                    }
                }, new Function<Integer, String>()
                {
                    public String apply(Integer integer)
                    {
                        return integer.toString();
                    }
                });

        assertEquals("1234", gOfF.apply("1234"));
    }
}

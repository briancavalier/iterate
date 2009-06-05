/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import junit.framework.TestCase;
import org.bc.iterate.Function;

public class CompositeFunctionTest extends TestCase
{
    public void testApply() throws Exception
    {
        final CompositeFunction<String, Integer, String> fOfG =
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

        assertEquals("1234", fOfG.apply("1234"));
    }
}

/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.BinaryFunction;
import org.junit.Assert;
import org.junit.Test;

public class CompositeBinaryFunctionTest
{
    @Test
    public void apply() throws Exception
    {
        final CompositeBinaryFunction<String, String, String, Integer> gOfF =
                new CompositeBinaryFunction<String, String, String, Integer>(
                        new BinaryFunction<String, Integer, String>()
                        {
                            public String apply(String s, Integer integer)
                            {
                                return s + integer;
                            }
                        }, new BinaryFunction<String, Integer, String>()
                        {
                            public String apply(String s, Integer integer)
                            {
                                return s + integer;
                            }
                        });

        Assert.assertEquals("a11", gOfF.apply("a", 1));
    }
}

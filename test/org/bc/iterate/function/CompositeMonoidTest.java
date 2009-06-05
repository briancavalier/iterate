/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.Function;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Arrays;

public class CompositeMonoidTest
{
    @Test
    public void apply() throws Exception
    {
        final CompositeMonoid<String> f =
                new CompositeMonoid<String>(Arrays.asList(new Function<String, String>()
                {
                    public String apply(String s)
                    {
                        return s.toUpperCase();
                    }
                }, new Function<String, String>()
                {
                    public String apply(String s)
                    {
                        return s.trim();
                    }
                }));

        assertEquals("TEST", f.apply("  test  "));
    }
}

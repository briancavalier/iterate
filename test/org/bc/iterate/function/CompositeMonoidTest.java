/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import junit.framework.TestCase;
import org.bc.iterate.Function;

import java.util.Arrays;

public class CompositeMonoidTest extends TestCase
{
    public void testApply() throws Exception
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

/*
 * Copyright (c) 2007-2009 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bc.iterate;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FunctionsTest
{
    @Test
    public void identity()
    {
        String s = "foo";
        final String t = Functions.<String>identity().apply(s);
        assertEquals(s, t);
    }

    @Test
    public void invoke() throws Exception
    {
        int i = 10;
        assertEquals("10", Functions.<Integer, String>invoke("toString").apply(i));

        String s = "  This is a test  ";
        final String result = Functions.<String, String>invoke("trim").apply(s);
        assertEquals("This is a test", result);
    }

    @Test
    public void invoke2() throws Exception
    {
        String s = "This is a test";
        assertEquals(s.substring(0, 4), Functions.<String, String>invoke("substring", 0, 4).apply(s));
        assertEquals(s.substring(0, 4), Functions.<String, String>invoke("substring", new Integer[]{0, 4}).apply(s));
    }

    @Test
    public void applyToEach()
    {
        Function<Iterable<Integer>, Iterable<Integer>> plusOne = Functions.applyToEach(new Function<Integer, Integer>()
        {
            @Override
            public Integer apply(Integer i)
            {
                return i + 1;
            }
        });

        assertEquals(Arrays.asList(2, 3, 4), plusOne.apply(Arrays.asList(1, 2, 3)));
    }
}

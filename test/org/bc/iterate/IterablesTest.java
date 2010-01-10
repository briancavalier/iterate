/*
 * Copyright (c) 2007-2010 Brian Cavalier
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

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author bcavalier
 */
public class IterablesTest
{
    @Test
    public void concat()
    {
        final List<String> src = Arrays.asList("a", "b", "c", "i", "j", "k", "c", "b", "a");
        final List<List<String>> nested = Arrays.asList(src.subList(0, 3), src.subList(3, 6), src.subList(6, 9));
        final List<String> results = new ArrayList<String>(10);
        for (String s : Iterables.concat(nested)) {
            results.add(s);
        }
        assertEquals(src, results);
    }

    @Test
    public void generate()
    {
        final List<Integer> results = new ArrayList<Integer>(10);
        for (Integer i : Iterables.generate(10, Integers.identity())) {
            results.add(i);
        }

        assertEquals(10, results.size());
    }

    @Test
    public void cycle()
    {
        final Iterator<Integer> cycle = Iterables.cycle(Arrays.asList(1, 2)).iterator();
        assertEquals(Integer.valueOf(1), cycle.next());
        assertEquals(Integer.valueOf(2), cycle.next());
        assertEquals(Integer.valueOf(1), cycle.next());
        assertEquals(Integer.valueOf(2), cycle.next());
        assertEquals(Integer.valueOf(1), cycle.next());
        assertEquals(Integer.valueOf(2), cycle.next());
    }
}

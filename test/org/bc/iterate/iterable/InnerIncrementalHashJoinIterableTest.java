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
package org.bc.iterate.iterable;

import org.bc.iterate.Function;
import org.bc.iterate.Integers;
import org.bc.iterate.Strings;
import org.bc.iterate.relational.JoinResult;
import org.bc.iterate.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InnerIncrementalHashJoinIterableTest
{
    @Test
    public void next()
    {
        InnerIncrementalHashJoinIterable<Integer, Integer, Integer> j =
                new InnerIncrementalHashJoinIterable<Integer, Integer, Integer>(
                        Arrays.asList(1, 2, 4, 5), Integers.identity(),
                        Arrays.asList(1, 2, 3, 4), Integers.identity());
        Map<Integer, Integer> results = new HashMap<Integer, Integer>(5);
        for (JoinResult<Integer, Integer, Integer> result : j) {
            results.put(result.getX(), result.getY());
        }

        assertEquals(Integer.valueOf(1), results.get(1));
        assertEquals(Integer.valueOf(2), results.get(2));
        assertEquals(Integer.valueOf(4), results.get(4));
        assertFalse(results.containsKey(3));
        assertFalse(results.containsKey(5));
    }

    @Test
    public void testNextDuplicateKeys() throws Exception
    {
        List<Integer> items = Arrays.asList(1, 2, 3, 4);
        List<String> toJoin = Arrays.asList("one", "two", "four", "four2");
        InnerIncrementalHashJoinIterable<String, Integer, String> j = new InnerIncrementalHashJoinIterable<String, Integer, String>(items, Strings.string(), toJoin, new Function<String, String>()
        {
            public String apply(String s)
            {
                return "one".equals(s) ? "1" : "two".equals(s) ? "2" : "4";
            }
        });
        List<Pair<Integer, String>> results = new ArrayList<Pair<Integer, String>>(5);
        for (Pair<Integer, String> joined : j) {
            results.add(new Pair<Integer, String>(joined.getX(), joined.getY()));
        }

        Assert.assertEquals(4, results.size());
    }
}
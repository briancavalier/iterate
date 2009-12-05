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
package org.bc.iterate.iterable;

import org.bc.iterate.Function;
import org.bc.iterate.function.ToString;
import org.bc.iterate.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class RightIncrementalHashJoinIterableTest
{
    @Test
    public void testNext() throws Exception {
        List<Integer> items = Arrays.asList(1, 2);
        List<String> toJoin = Arrays.asList("one", "two", "four");
        RightIncrementalHashJoinIterable<Integer, String, String> j = new RightIncrementalHashJoinIterable<Integer, String, String>(items, new ToString<Integer>(), toJoin, new Function<String, String>()
        {
            public String apply(String s)
            {
                return "one".equals(s) ? "1" : "two".equals(s) ? "2" : "4";
            }
        });
        Map<String, Integer> results = new HashMap<String, Integer>(4);
        for (Pair<Integer, String> joined : j) {
            results.put(joined.getY(), joined.getX());
        }

        Assert.assertEquals(3, results.size());
        Assert.assertEquals(new Integer(1), results.get("one"));
        Assert.assertEquals(new Integer(2), results.get("two"));
        // Left join should still contain items that did not appear in toJoin
        Assert.assertTrue(results.containsKey("four"));
        // But those items should have a null value for the right hand side
        Assert.assertNull(results.get("four"));
    }

    @Test
    public void testNextDuplicateKeys() throws Exception {
        List<Integer> items = Arrays.asList(1, 2, 3);
        List<String> toJoin = Arrays.asList("one", "two", "four");
        RightIncrementalHashJoinIterable<Integer, String, String> j = new RightIncrementalHashJoinIterable<Integer, String, String>(items, new ToString<Integer>(), toJoin, new Function<String, String>()
        {
            public String apply(String s)
            {
                return "one".equals(s) ? "1" : "2";
            }
        });
        List<Pair<Integer, String>> results = new ArrayList<Pair<Integer, String>>(3);
        for (Pair<Integer, String> joined : j) {
            results.add(new Pair<Integer, String>(joined.getX(), joined.getY()));
        }

        Assert.assertEquals(3, results.size());
    }
}
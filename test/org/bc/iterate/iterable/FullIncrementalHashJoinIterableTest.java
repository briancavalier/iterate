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

import org.bc.iterate.Integers;
import org.bc.iterate.relational.JoinResult;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FullIncrementalHashJoinIterableTest
{
    @Test
    public void next()
    {
        FullIncrementalHashJoinIterable<Integer, Integer, Integer> j =
                new FullIncrementalHashJoinIterable<Integer, Integer, Integer>(
                        Arrays.asList(1, 2, 4, 5), Integers.identity(),
                        Arrays.asList(1, 2, 3, 4), Integers.identity());
        Map<Integer, JoinResult<Integer, Integer, Integer>> results = new HashMap<Integer, JoinResult<Integer, Integer, Integer>>(5);
        for (JoinResult<Integer, Integer, Integer> result : j) {
            results.put(result.getKey(), result);
        }

        assertEquals(Integer.valueOf(1), results.get(1).getX());
        assertEquals(Integer.valueOf(1), results.get(1).getY());
        assertEquals(Integer.valueOf(2), results.get(2).getX());
        assertEquals(Integer.valueOf(2), results.get(2).getY());
        assertEquals(Integer.valueOf(4), results.get(4).getX());
        assertEquals(Integer.valueOf(4), results.get(4).getY());

        assertTrue(results.containsKey(3));
        assertEquals(Integer.valueOf(3), results.get(3).getY());
        assertNull(results.get(3).getX());

        assertTrue(results.containsKey(5));
        assertEquals(Integer.valueOf(5), results.get(5).getX());
        assertNull(results.get(5).getY());
    }
}
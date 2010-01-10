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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ArrayIterableTest
{
    @Test
    public void basic()
    {
        Integer[] expected = { 1, 3, 5, 7, 9 };
        ArrayIterable<Integer> iter = new ArrayIterable<Integer>(expected);

        int i = 0;
        for (Integer val : iter) {
            assertEquals(expected[i++], val);
        }
    }

    @SuppressWarnings({"UnusedAssignment", "ResultOfObjectAllocationIgnored"})
    @Test
    public void range()
    {
        Integer[] expected = { 1, 3, 5, 7, 9 };
        int start = 1;
        int end = expected.length - 1;
        ArrayIterable<Integer> iter = new ArrayIterable<Integer>(expected, start, end);

        int count = 0;
        int i = start;
        for (Integer val : iter) {
            assertEquals(expected[i++], val);
            count++;
        }

        assertEquals((end - start), count);

        try {
            new ArrayIterable<Integer>(expected, -1, end);
            fail("Should throw IllegalArgumentException when start < 0");
        } catch(IllegalArgumentException ignored) {}

        try {
            new ArrayIterable<Integer>(expected, start, expected.length+1);
            fail("Should throw IllegalArgumentException when end > array.length");
        } catch(IllegalArgumentException ignored) {}
    }
}

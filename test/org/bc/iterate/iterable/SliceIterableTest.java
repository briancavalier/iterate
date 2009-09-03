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

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SliceIterableTest
{
    @Test
    public void testHasNext()
    {
        List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Assert.assertTrue(new SliceIterable<Integer>(ints, 0, 10).hasNext());
        Assert.assertTrue(new SliceIterable<Integer>(ints, 0, 1).hasNext());
        Assert.assertTrue(new SliceIterable<Integer>(ints, 9, 10).hasNext());
    }

    @SuppressWarnings({"ResultOfObjectAllocationIgnored"})
    @Test(expected = IllegalArgumentException.class)
    public void testHasNextArguments1()
    {
        new SliceIterable<Integer>(Collections.<Integer>emptyList(), 1, 1);
    }

    @SuppressWarnings({"ResultOfObjectAllocationIgnored"})
    @Test(expected = IllegalArgumentException.class)
    public void testHasNextArguments2()
    {
        new SliceIterable<Integer>(Collections.<Integer>emptyList(), -1, 1);
    }

    @SuppressWarnings({"JUnitTestMethodWithNoAssertions"})
    @Test
    public void testNext()
    {
        List<Integer> ints = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        verifySlice(ints, 0, 10);
        verifySlice(ints, 0, 1);
        verifySlice(ints, 9, 10);
        verifySlice(ints, 9, 20, 1);
    }

    private static void verifySlice(List<Integer> ints, final int start, final int end)
    {
        verifySlice(ints, start, end, end-start);
    }

    private static void verifySlice(List<Integer> ints, final int start, final int end, final int expectedCount)
    {
        int count = 0;
        //noinspection UnusedDeclaration
        for (Integer i : new SliceIterable<Integer>(ints, start, end)) {
            count++;
        }
        //noinspection ImplicitNumericConversion
        Assert.assertEquals(expectedCount, count);
    }
}

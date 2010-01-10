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

import org.bc.iterate.Condition;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Arrays;

public class FilterIterableTest
{
    @Test
    public void basic()
    {
        assertHasCorrectElements(Arrays.asList(1, 2, 3, 2, 4, 5), 2, 2);
        assertHasCorrectElements(Arrays.asList(1, 2, 3, 2, 4, 5, 2), 2, 3);
        assertHasCorrectElements(Arrays.asList(2, 1, 2, 3, 2, 4, 5), 2, 3);
        assertHasCorrectElements(Arrays.asList(2, 1, 2, 3, 2, 4, 5, 2), 2, 4);
        assertHasCorrectElements(Arrays.asList(2, 1, 2, 3, 2, 4, 5, 2), 7, 0);

    }

    private static void assertHasCorrectElements(Iterable<Integer> values, final int value, final int expectedCount)
    {
        FilterIterable<Integer> iter = new FilterIterable<Integer>(values, new Condition<Integer>()
        {
            public boolean eval(Integer i)
            {
                return value == i;
            }
        });
        int count = 0;
        for (int i : iter) {
            assertEquals(value, i);
            count++;
        }
        Assert.assertEquals(expectedCount, count);
    }
}

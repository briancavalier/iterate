/*
 * Copyright 2007-2009 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bc.iterate.iterable;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class GroupIterableTest
{
    @Test
    public void basic()
    {
        assertCorrectGroups(new GroupIterable<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                                                       2), 5);

        assertCorrectGroups(new GroupIterable<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                                                       3), 4);
    }

    private static void assertCorrectGroups(GroupIterable<Integer> iter, final int numGroups)
    {
        int i = 0;
        int j = 0;
        for (Collection<Integer> group : iter) {
            for (final Integer val : group) {
                assertEquals(j++, val.intValue());
            }
            i++;
        }
        assertEquals(numGroups, i);
    }
}

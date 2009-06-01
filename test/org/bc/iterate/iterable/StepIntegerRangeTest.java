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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StepIntegerRangeTest
{
    @SuppressWarnings({"UnusedDeclaration"})
    @Test
    public void basic()
    {
        final int start = 10;
        final int end = 20;
        final int step = 3;
        IntegerRange range = new StepIntegerRange(start, end, step);
        int count = 0;
        for (int i : range) {
            count++;
        }

        assertEquals(4, count);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    @Test
    public void basic2()
    {
        final int start = 10;
        final int end = 23;
        final int step = 3;
        IntegerRange range = new StepIntegerRange(start, end, step);
        int count = 0;
        for (int i : range) {
            count++;
        }

        assertEquals(5, count);
    }

}

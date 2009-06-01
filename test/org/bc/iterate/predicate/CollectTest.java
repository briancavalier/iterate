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

package org.bc.iterate.predicate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectTest
{
    @Test
    public void basic()
    {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> results = new ArrayList<Integer>(expected.size());

        Collect<Integer> c = new Collect<Integer>();
        for (Integer i : expected) {
            c.apply(i, results);
        }

        assertEquals(expected, results);
    }
}

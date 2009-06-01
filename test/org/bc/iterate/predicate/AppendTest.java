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

import java.util.Arrays;
import java.util.List;

public class AppendTest
{
    @Test
    public void basic()
    {
        List<Integer> test = Arrays.asList(1, 2, 3, 4, 5);
        StringBuilder buf = new StringBuilder(32);

        Append<Integer> p = new Append<Integer>();
        for (Integer i : test) {
            p.apply(i, buf);
        }

        assertEquals("12345", buf.toString());
    }
}

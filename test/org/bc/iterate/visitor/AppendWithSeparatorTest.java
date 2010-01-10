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

package org.bc.iterate.visitor;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppendWithSeparatorTest
{
    @Test
    public void basic()
    {
        List<Integer> test = Arrays.asList(1, 2, 3, 4, 5);
        StringBuilder buf = new StringBuilder(32);

        AppendWithSeparator<Integer> p = new AppendWithSeparator<Integer>(",");
        for (Integer i : test) {
            p.visit(i, buf);
        }

        assertEquals("1,2,3,4,5", buf.toString());
    }

    @Test
    public void reset()
    {
        StringBuilder buf = new StringBuilder(32);

        AppendWithSeparator<Integer> p = new AppendWithSeparator<Integer>(",");
        p.visit(1, buf);
        p.visit(2, buf);
        p.reset();
        p.visit(3, buf);
        p.visit(4, buf);

        assertEquals("1,23,4", buf.toString());
    }
}
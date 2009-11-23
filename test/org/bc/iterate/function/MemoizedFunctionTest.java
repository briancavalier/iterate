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
package org.bc.iterate.function;

import org.bc.iterate.Function;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MemoizedFunctionTest
{
    @Test
    public void apply() throws Exception {
        IntToString f = new IntToString();

        MemoizedFunction<Integer, String> m = new MemoizedFunction<Integer, String>(f);
        assertEquals("1", m.apply(1));
        assertEquals("2", m.apply(2));
        assertEquals("1", m.apply(1));

        assertEquals(2, f.getCount());
    }

    private static class IntToString implements Function<Integer, String>
    {
        private int count = 0;

        public String apply(Integer integer)
        {
            count++;
            return integer.toString();
        }

        public int getCount()
        {
            return count;
        }
    }
}

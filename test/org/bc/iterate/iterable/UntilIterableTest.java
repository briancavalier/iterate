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

import org.bc.iterate.Condition;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UntilIterableTest
{
    @Test
    public void basic()
    {
        final List<String> values = Arrays.asList("a", "b", "c", "d", "e");
        UntilIterable<String> iter = new UntilIterable<String>(values, new Condition<String>()
        {
            public boolean eval(String s)
            {
                return "c".equals(s);
            }
        });

        int i=0;
        for (String s : iter) {
            Assert.assertEquals(values.get(i++), s);
        }

        Assert.assertEquals(2, i);
    }
}

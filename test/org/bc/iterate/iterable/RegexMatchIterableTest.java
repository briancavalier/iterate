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
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.io.StringReader;
import java.util.regex.Pattern;

public class RegexMatchIterableTest
{
    @Test
    public void basic()
    {
        String input = "this, that, and the other!";
        Pattern p = Pattern.compile("\\w+");
        RegexMatchIterable iter = new RegexMatchIterable(new StringReader(input), p);

        int count = 0;
        for (String s : iter) {
            assertTrue(s.matches("\\w+"));
            count++;
        }

        assertEquals(5, count);
    }
}

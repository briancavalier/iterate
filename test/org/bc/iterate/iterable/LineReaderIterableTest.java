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

import java.io.StringReader;

public class LineReaderIterableTest
{
    @Test
    public void basic()
    {
        final String lines = "This is\na test\nof reading\nlines";
        final String[] expected = lines.split("\\n");
        StringReader r = new StringReader(lines);
        LineReaderIterable ri = new LineReaderIterable(r);

        int i = 0;
        for (String s : ri) {
            assertEquals(expected[i++], s);
        }

        assertEquals(expected.length, i);
    }
}

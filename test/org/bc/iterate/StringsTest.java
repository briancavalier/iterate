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

package org.bc.iterate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StringsTest
{
    @Test
    public void join() throws IOException
    {
        assertEquals("foo,bar,baz", Strings.join(Arrays.asList("foo", "bar", "baz")));
        assertEquals("foobarbaz", Strings.join(Arrays.asList("foo", "bar", "baz"), ""));
        assertEquals("foo | bar | baz", Strings.join(Arrays.asList("foo", "bar", "baz"), " | "));

        assertEquals("foo,bar,baz", Strings.join(Arrays.asList("foo", "bar", "baz")));
        assertEquals("foobarbaz", Strings.join(Arrays.asList("foo", "bar", "baz"), ""));
        assertEquals("foo | bar | baz", Strings.join(Arrays.asList("foo", "bar", "baz"), " | "));


        assertEquals("foo,bar,baz", Strings.join(Arrays.asList("foo", "bar", "baz"),
                                                        new StringBuilder(32)).toString());
        assertEquals("foobarbaz", Strings.join(Arrays.asList("foo", "bar", "baz"),
                                                      new StringBuilder(32), "").toString());
        assertEquals("foo | bar | baz", Strings.join(Arrays.asList("foo", "bar", "baz"),
                                                            new StringBuilder(32), " | ").toString());

        assertEquals("foo,bar,baz", Strings.join(Arrays.asList("foo", "bar", "baz"),
                                                        new StringBuilder(32)).toString());
        assertEquals("foobarbaz", Strings.join(Arrays.asList("foo", "bar", "baz"),
                                                      new StringBuilder(32), "").toString());
        assertEquals("foo | bar | baz", Strings.join(Arrays.asList("foo", "bar", "baz"),
                                                            new StringBuilder(32), " | ").toString());

        assertEquals("", Strings.join(new ArrayList<String>(0)));
        assertEquals("", Strings.join(new ArrayList<String>(0), new StringBuilder(32)).toString());
    }

    @Test
    public void defaultJoinSeparator() throws IOException
    {
        Strings.setDefaultJoinSeparator(" | ");
        assertEquals("foo | bar", Strings.join(Arrays.asList("foo", "bar")));
        assertEquals("foo | bar", Strings.join(Arrays.asList("foo", "bar"), new StringBuilder(32)).toString());
    }

    @Test
    public void smartTitleCase()
    {
        assertEquals("Nothing to be Afraid Of", Strings.smartTitleCase("nothing to be afraid of"));
        assertEquals("The First Word is Capitalized", Strings.smartTitleCase("the first word is capitalized"));
        assertEquals("I Love My iPhone", Strings.smartTitleCase("i love my iPhone"));
        assertEquals("This is Why I Love My iPhone", Strings.smartTitleCase("this is why i love my iPhone"));

        assertEquals("Q&A", Strings.smartTitleCase("Q&A"));
        assertEquals("AT&T", Strings.smartTitleCase("AT&T"));
    }

    @Test
    public void titleCase()
    {
        assertEquals("Nothing To Be Afraid Of", Strings.titleCase("nothing to be afraid of"));
        assertEquals("The First Word Is Capitalized", Strings.titleCase("the first word is capitalized"));
        assertEquals("I Love My iPhone", Strings.titleCase("i love my iPhone"));
        assertEquals("This Is Why I Love My iPhone", Strings.titleCase("this is why i love my iPhone"));
    }
}

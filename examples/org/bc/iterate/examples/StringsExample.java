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
package org.bc.iterate.examples;

import org.bc.iterate.Function;
import org.bc.iterate.Strings;

import java.io.IOException;
import static java.util.Arrays.asList;

public class StringsExample
{
    public static void main(String[] args) throws IOException
    {
        // "Hello world"
        System.out.println(Strings.join(asList("Hello", "world"), " "));

        // Same, but code is nicer, imho
        Strings.join(asList("Hello", "world"), System.out, " ").println();

        // "hello world"
        Strings.join(asList("Hello", "world"), new Function<String, String>()
        {
            public String apply(String s)
            {
                return s.toLowerCase();
            }
        }, System.out, " ").println();

        // "Hello World", using the all-params version of join()
        Strings.join(asList("hello", "world"), Strings.capitalize(), System.out, " ").println();

        // "Hello to The World"
        System.out.println(Strings.smartTitleCase(Strings.join(asList("hello", "to", "the", "world"), " ")));
        System.out.println(Strings.smartTitleCase("hello to the world"));

        // "The World Says Hello Back to Me"
        System.out.println(Strings.smartTitleCase("the world says hello back to me"));
    }
}

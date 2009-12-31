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
package org.bc.iterate.examples;

import org.bc.iterate.Function;
import org.bc.iterate.visitor.AppendWithSeparator;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.bc.iterate.Iterate.append;
import static org.bc.iterate.Iterate.each;
import static org.bc.iterate.Strings.*;

public class StringsExample
{
    public static void main(String[] args) throws IOException
    {
        // "Hello world"
        System.out.println(join(asList("Hello", "world"), " "));

        // Same, but code is nicer, imho
        join(asList("Hello", "world"), System.out, " ").println();

        // "hello world"
        join(asList("Hello", "world"), new Function<String, String>()
        {
            public String apply(String s)
            {
                return s.toLowerCase();
            }
        }, System.out, " ").println();
        // Similarly, using straight Iterate--this is just one of many ways to do it.
        final AppendWithSeparator<String> appender = append(" ");
        each("Hello", "world").map(lowerCase()).visit(appender, System.out).println();

        // "Hello World", using the all-params version of join()
        join(asList("hello", "world"), capitalize(), System.out, " ").println();
        // Similarly, using straight Iterate--this is just one of many ways to do it.
        // Here, instead of an intermediate map() step, we set the appender's formatter to use the
        // Strings.capitalize Function. Because we've already used the appender, which is stateful in this case,
        // we have to use reset() to tell it that we're starting over, so that it won't append a separator before
        // the first item.
        each("hello", "world").visit(appender.reset().format(capitalize()), System.out).println();

        // "Hello to The World"
        System.out.println(smartTitleCase(join(asList("hello", "to", "the", "world"), " ")));
        System.out.println(smartTitleCase("hello to the world"));

        // "The World Says Hello Back to Me"
        System.out.println(smartTitleCase("the world says hello back to me"));
    }
}

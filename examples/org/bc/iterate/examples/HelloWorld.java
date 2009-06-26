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

import static org.bc.iterate.Iterate.append;
import static org.bc.iterate.Iterate.each;
import static org.bc.iterate.Strings.*;

public class HelloWorld
{
    public static void main(String[] args)
    {
        each("hell0", "foo", "bar", "w0rld!", "baz")
                .where(contains('0'))
                .map(replace('0', 'o'))
                .map(titleCase())
                .visit(append(" "), System.out).println();
    }
}
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

package org.bc.iterate.examples;

import static org.bc.iterate.Iterate.*;
import org.bc.iterate.iterable.LineReaderIterable;
import org.bc.iterate.Strings;

import java.io.IOException;

public class SedExample
{
    public static void main(String[] args) throws IOException
    {
        if(args.length < 2) {
            System.err.println("Usage: SedExample regex replacement file|url");
            System.exit(1);
        }

        final LineReaderIterable lines = (args.length >= 3) ? line(args[2]) : line(System.in);

        each(lines).map(Strings.replace(args[0], args[1])).visit(println(), System.out);
    }
}

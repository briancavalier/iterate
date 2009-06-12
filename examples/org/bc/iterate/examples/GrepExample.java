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

import static org.bc.iterate.Conditions.regex;
import static org.bc.iterate.Iterate.*;

import java.io.IOException;

public class GrepExample {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("Usage: GrepExample regex file|url");
            System.exit(1);
        }

        final Iterable<String> lines = (args.length >= 2) ? line(args[1]) : line(System.in);

        each(lines).where(regex(args[0])).visit(println(), System.out);
    }
}

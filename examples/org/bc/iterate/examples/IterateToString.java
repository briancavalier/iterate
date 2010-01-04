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

import org.bc.iterate.Conditions;
import org.bc.iterate.Integers;
import org.bc.iterate.Iterate;
import org.bc.iterate.Strings;

import static org.bc.iterate.Integers.*;
import static org.bc.iterate.Integers.range;
import static org.bc.iterate.Integers.sum;
import static org.bc.iterate.Iterate.each;
import static org.bc.iterate.Strings.*;

public class IterateToString
{
    public static void main(String[] args)
    {
        Iterate<Integer> i1 = each(range(1, 101).by(2)).where(Conditions.gt(50)).map(sum(), 1).map(multiply(), 2);
        System.out.println(i1);
        i1.println(System.out);

        Iterate<String> i2 = each(" foo ", " bar ", " baz ").map(trim()).map(capitalize());
        System.out.println(i2);
        i2.println(System.out);
    }
}

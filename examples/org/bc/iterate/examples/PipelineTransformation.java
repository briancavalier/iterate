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

import org.bc.iterate.Iterables;
import org.bc.iterate.Iterate;

import java.util.Arrays;

import static org.bc.iterate.Integers.identity;
import static org.bc.iterate.Integers.square;
import static org.bc.iterate.Iterables.append;
import static org.bc.iterate.Iterables.prepend;

public class PipelineTransformation
{
    public static void main(String[] args)
    {
        // First, square the items in the original list, then inject some new items before them in the pipeline
        // Then collect all items and print the resulting list
        final Iterate<Integer> ints = Iterables.generate(10, identity());
        System.out.println(ints.map(square()).prepend(Arrays.asList(1, 2, 3)).list());
        System.out.println(ints.map(square()).transform(prepend(Arrays.asList(1, 2, 3))).list());

        // First, square the items in the original list, then inject some new items after them in the pipeline
        // Then collect all items and print the resulting list
        System.out.println(ints.map(square()).append(Arrays.asList(1, 2, 3)).list());
        System.out.println(ints.map(square()).transform(append(Arrays.asList(1, 2, 3)))
                .transform(Iterables.<Integer>sort()).list());

        System.out.println(ints.map(square()).slice(1, 5).list());
        System.out.println(ints.map(square()).transform(Iterables.<Integer>slice(1, 5)).list());
        System.out.println(ints.map(square()).slice(1).list());
        System.out.println(ints.map(square()).transform(Iterables.<Integer>slice(1)).list());
    }
}

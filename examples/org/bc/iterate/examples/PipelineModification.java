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

import static org.bc.iterate.Algorithms.generate;
import static org.bc.iterate.Integers.*;
import static org.bc.iterate.Iterate.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PipelineModification
{
    public static void main(String[] args)
    {
        List<Integer> ints = generate(10, identity());
        System.out.println(each(range(10, 100)).visit(collect(), wrap(ints)).map(square()).reduce(sum()));

        // First, square the items in the original list, then inject some new items before them in the pipeline
        // Then collect all items and print the resulting list
        ints = generate(10, identity());
        System.out.println(each(ints).map(square()).prepend(Arrays.asList(1, 2, 3)).visit(collect(), new ArrayList<Integer>(13)));

        // First, square the items in the original list, then inject some new items after them in the pipeline
        // Then collect all items and print the resulting list
        ints = generate(10, identity());
        System.out.println(each(ints).map(square()).append(Arrays.asList(1, 2, 3)).visit(collect(), new ArrayList<Integer>(13)));

    }
}

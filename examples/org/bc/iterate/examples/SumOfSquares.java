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

import org.bc.iterate.BinaryFunction;
import org.bc.iterate.Function;
import org.bc.iterate.Integers;
import static org.bc.iterate.Functions.compose;
import static org.bc.iterate.Integers.square;
import static org.bc.iterate.Integers.sum;
import static org.bc.iterate.Iterate.each;

public class SumOfSquares
{
    public static void main(String[] args)
    {
        // These are all equivalent ways of computing sum of squares.
        // Each will print the same result, the sum of squared integers from 1 to 100: 338350

        // Using inline anonymous classes like lambda functions
        System.out.println(each(Integers.range(1, 101))
                .map(new Function<Integer, Integer>()
                {
                    public Integer apply(Integer i)
                    {
                        return i * i;
                    }
                })
                .reduce(new BinaryFunction<Integer, Integer, Integer>()
        {
            public Integer apply(Integer i1, Integer i2)
            {
                return i1 + i2;
            }
        }, 0));

        // Using builtin Integers functions and so are more compact
        System.out.println(each(Integers.range(1, 101))
                .map(square())
                .reduce(sum(), 0));

        // This one is my favorite--I feel it is the most readable
        System.out.println(each(Integers.range(1, 101))
                .map(square())
                .reduce(sum()));

        // Using only reduce() with a composed binary function
        // Looking more LISP-like ...
        System.out.println(each(Integers.range(1, 101))
                .reduce(compose(square(), sum()), 0));

        // And finally, the most compact, but most LISP-like
        System.out.println(each(Integers.range(1, 101))
                .reduce(compose(square(), sum())));

    }
}

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

import org.bc.iterate.Integers;
import org.bc.iterate.Iterate;
import org.bc.iterate.iterable.IntegerRange;

import static org.bc.iterate.Integers.*;
import static org.bc.iterate.Iterate.*;

/**
 * A solution to http://projecteuler.net/index.php?section=problems&id=6 using {@link Iterate}
 */
public class EurlerProblem6
{
    public static void main(String[] args)
    {
        // using Iterate
        // range is inclusive min, exclusive max
        // squared sum - sum of squares
        int answer1 = square().apply(each(range(1, 101)).reduce(sum())) - each(range(1, 101)).map(square()).reduce(sum());
//        answer1 = square().apply(range(1, 101).reduce(sum())) - range(1, 101).map(square()).reduce(sum());
        // now using straight Java
        int sum = 0;
        int sumOfSquares = 0;
        for(int i=1; i<=100; i++) {
            sum += i;
            sumOfSquares += i*i;
        }

        int answer2 = (sum*sum) - sumOfSquares;

        System.out.println("Iterate: " + answer1);
        System.out.println("Java: " + answer2);
    }
}

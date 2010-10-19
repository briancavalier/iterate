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
import java.math.BigInteger;
import static org.bc.iterate.Integers.range;

public class EulerProblem48
{
    public static void main(String[] args)
    {
        String s = range(1, 1001).reduce(new BinaryFunction<Integer, BigInteger, BigInteger>()
        {
            @Override
            public BigInteger apply(Integer i, BigInteger bi)
            {
                return bi.add(BigInteger.valueOf((long) i).pow(i));
            }
        }, BigInteger.ZERO).toString();
        System.out.println(s.substring(s.length() - 10, s.length()));

        BigInteger bi = BigInteger.ZERO;
        for(int i=1; i<=1000; i++) {
            bi = bi.add(BigInteger.valueOf((long) i).pow(i));
        }
        s = bi.toString();
        System.out.println(s.substring(s.length() - 10, s.length()));   
    }
}

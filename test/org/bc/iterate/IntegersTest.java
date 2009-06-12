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
package org.bc.iterate;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntegersTest
{
    @Test
    public void identity()
    {
        assertEquals(Integer.valueOf(1), Integers.identity().apply(1));
    }

    @Test
    public void parse()
    {
        assertEquals(Integer.valueOf(1), Integers.parse().apply("1"));
        assertEquals(Integer.valueOf(1234), Integers.parse().apply("1234"));
        assertEquals(Integer.valueOf(-1234), Integers.parse().apply("-1234"));
    }

    @Test
    public void sum()
    {
        assertEquals(Integer.valueOf(3), Integers.sum().apply(1, 2));
    }

    @Test
    public void multiply()
    {
        assertEquals(Integer.valueOf(12), Integers.multiply().apply(3, 4));
    }

    @Test
    public void pow()
    {
        assertEquals(Double.valueOf(8.0), Integers.pow().apply(2, 3.0));
    }

    @Test
    public void square()
    {
        assertEquals(Integer.valueOf(16), Integers.square().apply(4));
    }

    @Test
    public void cube()
    {
        assertEquals(Integer.valueOf(64), Integers.cube().apply(4));
    }
}

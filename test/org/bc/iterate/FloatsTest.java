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
package org.bc.iterate;

import org.junit.Test;
import static org.junit.Assert.*;

public class FloatsTest
{
    @Test
    public void identity()
    {
        assertEquals(Float.valueOf(1.0f), Floats.identity().apply(1.0f));
    }

    @Test
    public void parse()
    {
        assertEquals(Float.valueOf(1.0f), Floats.parse().apply("1"));
        assertEquals(Float.valueOf(12.34f), Floats.parse().apply("12.34"));
        assertEquals(Float.valueOf(-12.34f), Floats.parse().apply("-12.34"));
    }

    @Test
    public void sum()
    {
        assertEquals(Float.valueOf(3.5f), Floats.sum().apply(1.25f, 2.25f));
    }

    @Test
    public void multiply()
    {
        assertEquals(Float.valueOf(1.2f), Floats.multiply().apply(0.3f, 4.0f));
    }

    @Test
    public void pow()
    {
        assertEquals(Double.valueOf(Math.pow(2.5, 1.5)), Floats.pow().apply(2.5f, 1.5));
    }

    @Test
    public void square()
    {
        assertEquals(Float.valueOf(4.3f*4.3f), Floats.square().apply(4.3f));
    }

    @Test
    public void cube()
    {
        assertEquals(Float.valueOf(5.6f*5.6f*5.6f), Floats.cube().apply(5.6f));
    }
}
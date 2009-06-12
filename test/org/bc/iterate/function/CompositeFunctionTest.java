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
package org.bc.iterate.function;

import org.bc.iterate.Function;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompositeFunctionTest
{
    @Test
    public void apply() throws Exception
    {
        final CompositeFunction<String, Integer, String> gOfF =
                new CompositeFunction<String, Integer, String>(new Function<String, Integer>()
                {
                    public Integer apply(String s)
                    {
                        return Integer.parseInt(s);
                    }
                }, new Function<Integer, String>()
                {
                    public String apply(Integer integer)
                    {
                        return integer.toString();
                    }
                });

        assertEquals("1234", gOfF.apply("1234"));
    }
}

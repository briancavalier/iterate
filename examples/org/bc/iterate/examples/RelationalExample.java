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

import org.bc.iterate.Function;
import org.bc.iterate.Functions;
import org.bc.iterate.Iterate;
import org.bc.iterate.relational.Join;

import java.util.Arrays;
import java.util.List;

public class RelationalExample
{
    public static void main(String[] args)
    {
        List<Integer> a = Arrays.asList(4, 5, 6);
        List<Integer> b = Arrays.asList(7, 8, 9);
        Iterate.each(a).join(Join.left(Functions.index(), Functions.index()), b).println(System.out);
    }
}

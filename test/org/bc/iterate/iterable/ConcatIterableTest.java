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

package org.bc.iterate.iterable;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

/**
 * @author bcavalier
 */
public class ConcatIterableTest
{
    @Test
    public void next() throws Exception {
        List<Integer> src = asList(1, 2, 3, 4);
        Iterable<Integer> c = new ConcatIterable<Integer>(asList(src.subList(0, 2), new ArrayList<Integer>(0), src.subList(2, 4)));
        List<Integer> results = new ArrayList<Integer>(src.size());
        for (Integer i : c) {
            results.add(i);
        }

        assertEquals(src, results);

    }
}

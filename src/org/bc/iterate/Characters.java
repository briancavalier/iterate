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

import org.bc.iterate.function.Identity;
import org.bc.iterate.iterable.IterableBase;

import java.util.Random;

/**
 * This class provides {@link org.bc.iterate.Function}s and {@link org.bc.iterate.Visitor}s for {@code char}s and {@link Character}s.
 *
 * @author Brian Cavalier
 */
public class Characters
{
    public static Function<Character, Character> identity()
    {
        return new Identity<Character>();
    }
}
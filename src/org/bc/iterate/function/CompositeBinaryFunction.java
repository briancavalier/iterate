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
package org.bc.iterate.function;

import org.bc.iterate.BinaryFunction;

public class CompositeBinaryFunction<X, Y, Z, R> implements BinaryFunction<X, R, Z>
{
    final private BinaryFunction<X, R, Y> f;
    final private BinaryFunction<Y, R, Z> g;

    public CompositeBinaryFunction(BinaryFunction<X, R, Y> f,
                                   BinaryFunction<Y, R, Z> g)
    {
        this.f = f;
        this.g = g;
    }

    public Z apply(X x, R r)
    {
        return  g.apply(f.apply(x, r), r);
    }
}

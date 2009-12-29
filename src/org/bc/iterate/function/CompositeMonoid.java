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

import org.bc.iterate.Function;

import java.util.List;

public class CompositeMonoid<X> implements Function<X, X>
{
    final private List<Function<X, X>> functions;

    public CompositeMonoid(final List<Function<X, X>> functions)
    {
        this.functions = functions;
    }

    public X apply(X x)
    {
        X result = x;
        for (Function<X, X> f : functions) {
            result = f.apply(result);
        }

        return result;
    }
}
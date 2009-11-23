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
package org.bc.iterate.function;

import org.bc.iterate.Function;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MemoizedFunction<X, Y> implements Function<X, Y>
{
    private final Map<X, Y> cachedResults;

    private final Function<X, Y> memoizedFunction;

    public MemoizedFunction(Function<X, Y> functionToMemoize)
    {
        this(functionToMemoize, new HashMap<X, Y>(128));
    }

    @SuppressWarnings({"AssignmentToCollectionOrArrayFieldFromParameter"})
    public MemoizedFunction(final Function<X, Y> functionToMemoize, final Map<X, Y> resultsCache)
    {
        this.memoizedFunction = functionToMemoize;
        this.cachedResults = resultsCache;
    }

    public MemoizedFunction(final Function<X, Y> functionToMemoize, int resultsCacheSize)
    {
        this.memoizedFunction = functionToMemoize;
        cachedResults = new LinkedHashMap<X,Y>(resultsCacheSize, 0.75f, true);
    }

    public Y apply(X x)
    {
        Y y = cachedResults.get(x);
        if(y == null) {
            y = memoizedFunction.apply(x);
            cachedResults.put(x, y);
        }

        return y;
    }
}

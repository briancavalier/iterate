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
package org.bc.iterate.condition;

import org.bc.iterate.Condition;
import org.bc.iterate.Function;

public class FunctionCondition<X, Y> implements Condition<X>
{
    private final Function<X, Y> f;

    private final Condition<Y> yCondition;

    public FunctionCondition(Function<X, Y> f, Condition<Y> yCondition)
    {
        this.f = f;
        this.yCondition = yCondition;
    }

    public boolean eval(X x)
    {
        return yCondition.eval(f.apply(x));
    }
}

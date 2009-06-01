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

package org.bc.iterate.predicate;

import org.bc.iterate.BinaryPredicate;
import org.bc.iterate.Predicate;

public class BindParam<X, Y> implements Predicate<X>
{
    private final Y param;
    private final BinaryPredicate<X, Y> callee;

    public BindParam(final Y param, final BinaryPredicate<X, Y> callee)
    {
        this.param = param;
        this.callee = callee;
    }

    public void apply(X x)
    {
        callee.apply(x, param);
    }
}

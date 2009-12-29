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

import org.bc.iterate.visitor.BindParam;
import org.bc.iterate.visitor.UnbindParam;

/**
 * Helper methods for {@link Visitor}s
 *
 * @author Brian Cavalier
 */
public class Visitors
{
    public static <X, Y> Visitor<X> bind(Y param, BinaryVisitor<X, Y> callee)
    {
        return new BindParam<X, Y>(param, callee);
    }

    public static <X, Y> BinaryVisitor<X, Y> unbind(Visitor<X> callee)
    {
        return new UnbindParam<X, Y>(callee);
    }
}

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

/**
 * A {@code BinaryFunction} represents a mathematical binary function {@code f(x,y) = z}, and is one of the main
 * concepts used in the Iterate library.  {@code BinaryFunction}s can be applied to items in an {@link Iterable} using
 * {@link Iterate#map(BinaryFunction, Object)}, used to reduce items using {@link Iterate#reduce(BinaryFunction,
 * Object)}, and can be composed using the various {@code compose(...)} methods in {@link org.bc.iterate.Functions}.
 *
 * @author Brian Cavalier
 * @param <X> argument 1 type
 * @param <Y> argument 2 type
 * @param <Z> result type
 */
public interface BinaryFunction<X, Y, Z>
{
    Z apply(X x, Y y);
}

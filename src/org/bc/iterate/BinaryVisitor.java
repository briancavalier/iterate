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

package org.bc.iterate;

/**
 * {@code BinaryVisitor} represents a variant of the Visitor design pattern that can be used to process (visit) items in
 * an {@link Iterable} using {@link Iterate#visit(BinaryVisitor, Object)}, but also receives a second argument {@code y}
 * that it can use in performing processing on items.
 *
 * @author Brian Cavalier
 * @param <X> type of item to visit
 * @param <Y> additional argument type
 */
public interface BinaryVisitor<X, Y>
{
    /**
     * Process the item {@code x} using the additional argument {@code y} for reference.
     * @param x item to visit
     * @param y additional reference argument
     */
    void visit(X x, Y y);
}

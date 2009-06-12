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
 * {@code Visitor} represents the typical Visitor design pattern, and can be used to process (visit) items in an {@link
 * Iterable} using {@link Iterate#visit(Visitor)}.
 *
 * @author Brian Cavalier
 * @param <T> type of item to visit
 */
public interface Visitor<T>
{
    /**
     * Process the item {@code t} 
     * @param t item to visit
     */
    void visit(T t);
}

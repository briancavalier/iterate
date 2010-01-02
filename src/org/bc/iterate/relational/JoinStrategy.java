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

package org.bc.iterate.relational;

import org.bc.iterate.relational.JoinResult;

/**
 * A {@link JoinStrategy} represents an algorithm for performing a relational join on the items in two {@link Iterable}s
 * @author Brian Cavalier
 */
public interface JoinStrategy<K, X, Y>
{
    /**
     * Performs a relational join on the two supplied {@link Iterable}s
     * @param left items on the left side of the relational join
     * @param right items on the right side of the relational join
     * @return an {@link Iterable} of {@link JoinResult}s of performing the join
     */
    Iterable<JoinResult<K, X, Y>> join(Iterable<X> left, Iterable<Y> right);
}

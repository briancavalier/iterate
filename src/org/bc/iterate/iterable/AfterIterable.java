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
package org.bc.iterate.iterable;

import org.bc.iterate.Condition;

import java.util.Iterator;

public class AfterIterable<X> extends FilterIterable<X>
{
    private boolean found = false;

    public AfterIterable(Iterable<X> items, Condition<? super X> condition)
    {
        super(items, condition);
    }

    @SuppressWarnings({"RefusedBequest"})
    @Override
    protected X findNext()
    {
        if (!found && iterator.hasNext()) {
            found = filter.eval(iterator.next());
        }

        X next = end();
        while (iterator.hasNext()) {
            next = iterator.next();
            if (filter.eval(next)) {
                next = end();
                found = true;
            }
        }
        return next;
    }
}

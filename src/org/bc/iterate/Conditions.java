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

import org.bc.iterate.condition.RegexMatch;
import org.bc.iterate.condition.FunctionCondition;

import java.util.Collection;
import java.util.regex.Pattern;

public class Conditions
{
    public static Condition<Object> isNull()
    {
        return new Condition<Object>()
        {
            public boolean eval(Object o)
            {
                return o == null;
            }
        };
    }

    public static Condition<Object> notNull()
    {
        return new Condition<Object>()
        {
            public boolean eval(Object o)
            {
                return o != null;
            }
        };
    }

    public static <X> Condition<X> eq(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) == 0;
            }
        };
    }

    public static <X> Condition<X> neq(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) != 0;
            }
        };
    }

    public static <X> Condition<X> gt(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) < 0;
            }
        };
    }

    public static <X> Condition<X> lt(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) > 0;
            }
        };
    }

    public static <X> Condition<X> before(final Comparable<X> comparable)
    {
        return lt(comparable);
    }

    public static <X> Condition<X> after(final Comparable<X> comparable)
    {
        return gt(comparable);
    }

    public static <X> Condition<X> gte(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) <= 0;
            }
        };
    }

    public static <X> Condition<X> lte(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) >= 0;
            }
        };
    }

    /**
     * @return a {@link Condition} that returns true iff {@code collection} contains the item passed to its {@link
     *         Condition#eval(Object)} method.
     */
    public static <X> Condition<X> in(final Collection<X> collection)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return collection.contains(x);
            }
        };
    }

    public static Condition<String> regex(Pattern p)
    {
        return new RegexMatch(p);
    }

    public static Condition<String> regex(String pattern)
    {
        return new RegexMatch(Pattern.compile(pattern));
    }

    public static <X> Condition<X> not(final Condition<X> condition)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return !condition.eval(x);
            }
        };
    }

    public static <X> Condition<X> and(final Condition<X> c1, final Condition<X> c2)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return c1.eval(x) && c2.eval(x);
            }
        };
    }

    public static <X> Condition<X> or(final Condition<X> c1, final Condition<X> c2)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return c1.eval(x) || c2.eval(x);
            }
        };
    }

    public static <X> Condition<X> xor(final Condition<X> c1, final Condition<X> c2)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return c1.eval(x) ^ c2.eval(x);
            }
        };
    }

    public static <X> Condition<X> all(final Condition<X>... conditions)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                for (Condition<X> condition : conditions) {
                    if (!condition.eval(x)) {
                        return false;
                    }
                }

                return true;
            }
        };
    }

    public static <X> Condition<X> none(final Condition<X>... conditions)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                for (Condition<X> condition : conditions) {
                    if (condition.eval(x)) {
                        return false;
                    }
                }

                return true;
            }
        };
    }

    public static <X> Condition<X> one(final Condition<X>... conditions)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                for (Condition<X> condition : conditions) {
                    if (condition.eval(x)) {
                        return true;
                    }
                }

                return false;
            }
        };
    }

    public static Condition chance(final double chance)
    {
        return new Condition() {

            public boolean eval(Object x) {
                return Math.random() < chance;
            }
        };
    }

    public static <X, Y> Condition<X> compose(final Function<X, Y> f, final Condition<Y> yCondition)
    {
        return new FunctionCondition<X, Y>(f, yCondition);
    }
}

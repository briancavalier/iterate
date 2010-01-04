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

import org.bc.iterate.condition.FunctionCondition;
import org.bc.iterate.condition.RegexMatch;

import java.util.Collection;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Provides a useful set of {@link Condition}s
 */
public class Conditions
{
    /**
     * {@link Condition} that returns true when evaluated for null objects, false otherwise.
     *
     * @return a {@link Condition} that returns true when evaluated for null objects, false otherwise.
     */
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

    /**
     * {@link Condition} that returns true when evaluated for non-null objects, false otherwise.
     *
     * @return a {@link Condition} that returns true when evaluated for non-null objects, false otherwise.
     */
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

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) == 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) == 0}, false otherwise.
     */
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

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) != 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) != 0}, false otherwise.
     */
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

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) < 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) < 0}, false otherwise.
     */
    public static <X> Condition<X> gt(final Comparable<X> comparable)
    {
        return new Condition<X>()
        {
            public boolean eval(X x)
            {
                return comparable.compareTo(x) < 0;
            }

            @Override
            public String toString()
            {
                return "> " + comparable;
            }
        };
    }

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) > 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) > 0}, false otherwise.
     */
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

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) < 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) < 0}, false otherwise.
     * @see #lt(Comparable)
     */
    public static <X> Condition<X> before(final Comparable<X> comparable)
    {
        return lt(comparable);
    }

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) > 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) > 0}, false otherwise.
     * @see #gt(Comparable)
     */
    public static <X> Condition<X> after(final Comparable<X> comparable)
    {
        return gt(comparable);
    }

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) <= 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) <= 0}, false otherwise.
     */
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

    /**
     * {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) >= 0}, false otherwise.
     *
     * @param comparable {@link Comparable} to which to compare evaluated objects.
     * @return a {@link Condition} that returns true when evaluated for objects for which {@code comparable.compareTo(object) >= 0}, false otherwise.
     */
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
     * {@link Condition} that returns true iff {@code collection} contains the item passed to its {@link
     * Condition#eval(Object)} method.
     *
     * @param collection items will be checked for membership in this {@link Collection}
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

    /**
     * {@link Condition} that returns true for items that match the supplied {@link Pattern}, false otherwise.
     *
     * @param p {@link} Pattern to match against evaluated items
     * @return a {@link Condition} that returns true for items that match the supplied {@link Pattern}, false otherwise.
     */
    public static Condition<String> regex(Pattern p)
    {
        return new RegexMatch(p);
    }

    /**
     * {@link Condition} that returns true for items that match the supplied regular expression
     * {@code pattern}, false otherwise.
     *
     * @param pattern pattern to match against evaluated items.  This will be compiled only once.
     * @return a {@link Condition} that returns true for items that match the supplied regular expression
     *         {@code pattern}, false otherwise.
     */
    public static Condition<String> regex(String pattern)
    {
        return new RegexMatch(Pattern.compile(pattern));
    }

    /**
     * {@link Condition} that returns true when {@code condition} returns false, and vice versa.
     *
     * @param condition {@link Condition} to negate
     * @return a {@link Condition} that returns true when {@code condition} returns false, and vice versa.
     */
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

    /**
     * {@link Condition} that returns true for items for which both {@code c1} and {@code c2} return true,
     * false otherwise.
     *
     * @param c1 first {@link Condition} to test
     * @param c2 second {@link Condition} to test
     * @return a {@link Condition} that returns true for items for which both {@code c1} and {@code c2} return true,
     *         false otherwise.
     */
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

    /**
     * a {@link Condition} that returns true for items for which either {@code c1} or {@code c2}, or both
     * return true, false otherwise.
     *
     * @param c1 first {@link Condition} to test
     * @param c2 second {@link Condition} to test
     * @return a {@link Condition} that returns true for items for which either {@code c1} or {@code c2}, or both
     *         return true, false otherwise.
     */
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

    /**
     * a {@link Condition} that returns true for items for which <em>exactly one</em> of {@code c1} or
     * {@code c2}, but not both return true, false otherwise.
     *
     * @param c1 first {@link Condition} to test
     * @param c2 second {@link Condition} to test
     * @return a {@link Condition} that returns true for items for which <em>exactly one</em> of {@code c1} or
     *         {@code c2}, but not both return true, false otherwise.
     */
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

    /**
     * a {@link Condition} that returns true for items for which <em>all</em> of the supplied {@code conditions}
     * return true.
     *
     * @param conditions list of {@link Conditions} to test
     * @return a {@link Condition} that returns true for items for which <em>all</em> of the supplied {@code conditions}
     *         return true.
     */
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

    /**
     * a {@link Condition} that returns true for items for which <em>all</em> of the supplied {@code conditions}
     * return <em>false</em>.  Note that this is equivalent to {@code all(not(c1), not(c2), ...)}, but different from
     * {@code not(all(c1,c2,...)}
     *
     * @param conditions list of {@link Conditions} to test
     * @return a {@link Condition} that returns true for items for which <em>all</em> of the supplied {@code conditions}
     *         return <em>false</em>.
     */
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

    /**
     * a {@link Condition} that returns true for items for which at least one of the supplied {@code conditions}
     * return true.
     *
     * @param conditions list of {@link Conditions} to test
     * @return a {@link Condition} that returns true for items for which at least one of the supplied {@code conditions}
     *         return true.
     */
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

    /**
     * a {@link Condition} that returns true a specified percentage of times it is evaluated.
     *
     * @param chance percentage ({@code 0 <= chance <= 1.0}) chance that the returned {@link Condition} will return true.
     * @return a {@link Condition} that returns true a specified percentage of times it is evaluated.
     */
    public static Condition<Object> chance(final double chance)
    {
        return chance(chance, new Random());
    }

    /**
     * a {@link Condition} that returns true a specified percentage of times it is evaluated.
     *
     * @param chance percentage ({@code 0 <= chance <= 1.0}) chance that the returned {@link Condition} will return true.
     * @param random {@link Random} to use for generating random numbers that are tested against {@code chance}
     * @return a {@link Condition} that returns true a specified percentage of times it is evaluated.
     */
    public static Condition<Object> chance(final double chance, final Random random)
    {
        return new Condition<Object>()
        {

            public boolean eval(Object x)
            {
                return random.nextDouble() < chance;
            }
        };
    }

    /**
     * a {@link Condition} that will return the result of evaluating {@code yCondition} on the result of
     * applying {@code f} to the item being evaluated.  Equivalent to {@code yCondition.eval(f.apply(x))}
     *
     * @param f          {@link Function} to apply to each item, the result of which is evaluated using {@code yCondition}
     * @param yCondition {@link Condition} to evaluate on the result of applying {@code f} to an item
     * @return a {@link Condition} that will return the result of evaluating {@code yCondition} on the result of
     *         applying {@code f} to the item being evaluated.
     * @see FunctionCondition
     */
    public static <X, Y> Condition<X> compose(final Function<X, Y> f, final Condition<Y> yCondition)
    {
        return new FunctionCondition<X, Y>(f, yCondition);
    }
}

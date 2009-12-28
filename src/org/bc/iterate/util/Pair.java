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
package org.bc.iterate.util;

import org.bc.iterate.Function;

public class Pair<X, Y>
{
    private final X x;
    private final Y y;

    public Pair(final X x, final Y y)
    {
        this.x = x;
        this.y = y;
    }

    public X getX()
    {
        return x;
    }

    public Y getY()
    {
        return y;
    }

    public static class X<X, Y> implements Function<Pair<X, Y>, X>
    {
        public X apply(Pair<X, Y> pair)
        {
            return pair.getX();
        }
    }

    public static class Y<X, Y> implements Function<Pair<X, Y>, Y>
    {

        public Y apply(Pair<X, Y> pair)
        {
            return pair.getY();
        }
    }

    @Override
    public String toString()
    {
        return "Pair{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }

        final Pair pair = (Pair) o;

        if (x != null ? !x.equals(pair.x) : pair.x != null) {
            return false;
        }
        if (y != null ? !y.equals(pair.y) : pair.y != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}

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

import org.bc.iterate.util.Pair;

public class JoinResult<K, X, Y> extends Pair<X, Y>
{
    private final K key;

    public JoinResult(K key, X x, Y y)
    {
        super(x, y);
        this.key = key;
    }

    public K getKey()
    {
        return key;
    }

    @Override
    public String toString()
    {
        return "JoinResult{" +
               "key=" + key +
               ", x=" + getX() +
               ", y=" + getY() +
               '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoinResult)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final JoinResult that = (JoinResult) o;

        if (key != null ? !key.equals(that.key) : that.key != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }
}

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

package org.bc.iterate.visitor;

import org.bc.iterate.BinaryVisitor;
import org.bc.iterate.Function;
import org.bc.iterate.Strings;

import java.io.IOException;

public class Append<X> implements BinaryVisitor<X, Appendable>
{
    protected Function<X, String> toString = Strings.string();

    public void visit(X x, Appendable appendable)
    {
        try {
            appendable.append(toString.apply(x));
        } catch (IOException e) {
            throw new RuntimeException("IOException trying to append item", e);
        }
    }

    public AppendWithSeparator<X> with(String separator)
    {
        return new AppendWithSeparator<X>(separator);
    }

    public Append<X> format(final Function<X, String> toString)
    {
        this.toString = toString;
        return this;
    }
}

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

import org.bc.iterate.Function;
import org.bc.logging.Logger;
import org.bc.logging.LoggerFactory;

import java.io.IOException;

/**
 * Subclass of {@link Append} that places a separator string before all appended items except the first.
 *
 * @author Brian Cavalier
 */
public class AppendWithSeparator<X> extends Append<X>
{
    private final String separator;
    private boolean sep = false;

    public AppendWithSeparator(String separator)
    {
        this.separator = separator;
    }

    public void visit(X x, Appendable appendable)
    {
        try {
            if (sep) {
                appendable.append(separator);
            }
            appendable.append(toString.apply(x));
            sep = true;
        } catch (IOException e) {
            throw new RuntimeException("IOException trying to append item", e);
        }
    }

    /**
     * After {@code reset()} is called, no separator will be appended before the item in the next call to
     * {@link #visit(Object, Appendable)}.
     *
     * @return this
     */
    public AppendWithSeparator<X> reset()
    {
        sep = false;
        return this;
    }

    @Override
    public AppendWithSeparator<X> format(final Function<X, String> toString)
    {
        super.format(toString);
        return this;
    }
}

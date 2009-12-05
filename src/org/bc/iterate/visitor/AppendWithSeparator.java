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
package org.bc.iterate.visitor;

import java.io.IOException;

/**
 * Subclass of {@link Append} that places a separator string before all appended items except the first.
 *
 * @author Brian Cavalier
 */
public class AppendWithSeparator<X> extends Append<X>
{
    private final String separator;
    private AppendStep appendStep = new AppendStep();

    public AppendWithSeparator(String separator)
    {
        this.separator = separator;
    }

    public void visit(X x, Appendable appendable)
    {
        try {
            appendStep = appendStep.append(x, appendable);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * After {@code reset()} is called, no separator will be appended before the item in the next call to
     * {@link #visit(Object, Appendable)}.
     */
    public AppendWithSeparator reset()
    {
        appendStep = new AppendStep();
        return this;
    }

    private class AppendStep {

        public AppendStep append(X x, Appendable appendable) throws IOException {
            appendable.append(x.toString());
            return new SeparatorAppendStep();
        }
    }

    private class SeparatorAppendStep extends AppendStep {
        @Override
        public AppendStep append(X x, Appendable appendable) throws IOException {
            appendable.append(separator).append(x.toString());
            return this;
        }
    }
}

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

import org.bc.iterate.Iterate;

import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RegexMatchIterable extends Iterate<String>
{
    private final Scanner scanner;
    private final Pattern pattern;
    private int maxLookahead;

    @Override
    public Iterator<String> iterator()
    {
        return new RegexMatchIterator();
    }

    public RegexMatchIterable(Readable input, Pattern tokenRegex)
    {
        this(input, tokenRegex, 0);
    }

    public RegexMatchIterable(Readable input, Pattern tokenRegex, int maxLookahead)
    {
        this.scanner = new Scanner(input);
        this.pattern = tokenRegex;
        this.maxLookahead = maxLookahead;
    }

    public RegexMatchIterable setLookahead(int maxLookahead)
    {
        this.maxLookahead = maxLookahead;
        return this;
    }

    private class RegexMatchIterator extends LookaheadIterator<String>
    {
        private final int horizon = RegexMatchIterable.this.maxLookahead;

        @Override
        protected String findNext()
        {
            return scanner.findWithinHorizon(pattern, horizon);
        }
    }
}

/*
 * Copyright 2007-2009 Brian Cavalier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bc.iterate.iterable;

import java.util.Scanner;
import java.util.regex.Pattern;

public class RegexMatchIterable extends LookaheadIterable<String>
{
    private Scanner scanner;
    private Pattern pattern;
    private int maxLookahead;

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

    protected String findNext()
    {
        return scanner.findWithinHorizon(pattern, maxLookahead);
    }

    public RegexMatchIterable setLookahead(int maxLookahead)
    {
        this.maxLookahead = maxLookahead;
        return this;
    }
}

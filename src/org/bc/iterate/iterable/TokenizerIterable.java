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

import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TokenizerIterable implements Iterable<String>
{
    private final Scanner scanner;

    @Override
    public Iterator<String> iterator()
    {
        return new TokenizerIterator();
    }

    public TokenizerIterable(Readable input, Pattern delimiter)
    {
        this.scanner = new Scanner(input).useDelimiter(delimiter);
    }

    private class TokenizerIterator extends AbstractIterator<String>
    {
        public boolean hasNext()
        {
            return scanner.hasNext();
        }

        public String next()
        {
            return scanner.next();
        }
    }
}

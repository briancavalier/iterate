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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class LineReaderIterable extends Iterate<String> implements Closeable
{
    private BufferedReader reader;

    private boolean close;

    public LineReaderIterable(Reader reader)
    {
        this(reader, true);
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public LineReaderIterable(Reader reader, boolean close)
    {
        this.reader = new BufferedReader(reader);
        this.close = close;
    }

    @Override
    public Iterator<String> iterator()
    {
        return new LineReaderIterator();
    }

    public LineReaderIterable setClose(boolean close)
    {
        this.close = close;
        return this;
    }

    public void close() throws IOException
    {
        reader.close();
    }

    private class LineReaderIterator extends LookaheadIterator<String>
    {
        protected String findNext()
        {
            try {
                return reader.readLine();
            } catch (IOException ignored) {
                if (close) {
                    //noinspection UnusedCatchParameter
                    try {
                        close();
                    } catch (IOException e) {
                        // oh well, we tried
                    }
                }
                return end();
            }
        }
    }
}

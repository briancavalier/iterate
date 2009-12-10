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

package org.bc.iterate.iterable;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ByteBufferIterable extends LookaheadIterable<ByteBuffer> implements Closeable
{
    private final ByteBuffer buffer;

    private boolean close;

    private final ReadableByteChannel channel;

    public ByteBufferIterable(InputStream in, int maxBytesPerIteration, boolean close)
    {
        this(Channels.newChannel(in), maxBytesPerIteration, close);
    }

    public ByteBufferIterable(ReadableByteChannel channel, int maxBytesPerIteration, boolean close)
    {
        this.channel = channel;
        this.close = close;
        this.buffer = ByteBuffer.allocate(maxBytesPerIteration);
    }

    protected ByteBuffer findNext()
    {
        try {
            final int bytesRead = channel.read(buffer);
            return (bytesRead == -1) ? end() : buffer.asReadOnlyBuffer();
        } catch (IOException ignored) {
            if (close) {
                //noinspection UnusedCatchParameter
                try {
                    close();
                } catch (IOException e) {
                    // oh well, we tried
                }
            }
            return null;
        }
    }

    public ByteBufferIterable setClose(boolean close)
    {
        this.close = close;
        return this;
    }

    public void close() throws IOException
    {
        channel.close();
    }
}
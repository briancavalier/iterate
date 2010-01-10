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
package org.bc.iterate.net;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.nio.charset.Charset;

public class UrlsTest
{
    @Test
    public void detectCharset()
    {
        // Negative cases
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset(null));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset(""));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset("foo"));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset("foo;bar"));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset("charset=bar"));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset("foo;blah=bar"));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset("foo;charset bar"));
        assertEquals(Urls.DEFAULT_CHARSET, Urls.detectCharset("foo;charset="));

        // Positive cases
        Charset expected = Charset.forName("iso-8859-1");
        assertEquals(expected, Urls.detectCharset("foo;charset=iso-8859-1"));
        assertEquals(expected, Urls.detectCharset(";charset=iso-8859-1"));
        assertEquals(expected, Urls.detectCharset("foo ; charset = iso-8859-1"));
        assertEquals(expected, Urls.detectCharset("foo ;    charset   =    iso-8859-1"));
    }
}

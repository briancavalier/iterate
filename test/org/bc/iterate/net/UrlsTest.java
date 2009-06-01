/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.net;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

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
        assertEquals("iso-8859-1", Urls.detectCharset("foo;charset=iso-8859-1"));
        assertEquals("iso-8859-1", Urls.detectCharset(";charset=iso-8859-1"));
        assertEquals("iso-8859-1", Urls.detectCharset("foo ; charset = iso-8859-1"));
        assertEquals("iso-8859-1", Urls.detectCharset("foo ;    charset   =    iso-8859-1"));
    }
}

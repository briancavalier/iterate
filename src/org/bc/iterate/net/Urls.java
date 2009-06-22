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

package org.bc.iterate.net;

import org.bc.iterate.Function;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Urls
{
    public static final String DEFAULT_CHARSET_STRING = "UTF-8";
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_STRING);
    public static final String ACCEPT_CHARSET_HEADER = "accept-encoding";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("^.*?;\\s*charset\\s*\\=\\s*(\\S+).*$");

    public static Function<String, String> get()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                try {
                    return get(s);
                } catch (IOException e) {
                    System.err.println(e);
                    return e.toString();
                }
            }
        };
    }

    public static String get(String url, String... headers) throws IOException
    {
        StringBuilder content = new StringBuilder(2048);
        CharBuffer buf = ByteBuffer.allocate(2048).asCharBuffer();
        Reader r = reader(url, headers);
        while(r.read(buf) != -1) {
            content.append(buf);
        }

        return content.toString();
    }

    public static Reader reader(String url, String... headers)
            throws IOException
    {
        final URLConnection urlConnection = openConnection(url);

        // Set any supplied headers
        for (String header : headers) {
            String[] nameAndValue = header.split(":");
            if (nameAndValue.length > 0) {
                urlConnection.setRequestProperty(nameAndValue[0].trim(), nameAndValue[1].trim());
            }
        }

        Charset cs = detectCharset(urlConnection.getContentType());
        return new InputStreamReader(urlConnection.getInputStream(), cs);
    }

    public static Reader reader(String url, Charset charset, String... headers)
            throws IOException
    {
        final URLConnection urlConnection = openConnection(url);

        // Set any supplied headers
        boolean needCharset = true;
        for (String header : headers) {
            String[] nameAndValue = header.split(":");
            if (nameAndValue.length > 0) {
                final String name = nameAndValue[0].trim();
                urlConnection.setRequestProperty(name, nameAndValue[1].trim());
                if (ACCEPT_CHARSET_HEADER.equals(name.toLowerCase())) {
                    needCharset = false;
                }
            }
        }

        if (needCharset) {
            urlConnection.setRequestProperty("Accept-Charset", charset.name());
        }

        return new InputStreamReader(urlConnection.getInputStream(),
                                     detectCharset(urlConnection.getContentType(), charset));
    }

    public static Reader reader(String url, Map<String, String> headers) throws IOException
    {
        final URLConnection urlConnection = openConnection(url);

        // Set any supplied headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            urlConnection.setRequestProperty(header.getKey(), header.getValue());
        }

        return new InputStreamReader(urlConnection.getInputStream(), detectCharset(urlConnection.getContentType()));
    }

    public static Reader reader(String url, Charset charset, Map<String, String> headers)
            throws IOException
    {
        final URLConnection urlConnection = openConnection(url);

        // Set any supplied headers
        if(!headers.containsKey(ACCEPT_CHARSET_HEADER)) {
            urlConnection.setRequestProperty("Accept-Charset", charset.name());
        }

        // Set any supplied headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            urlConnection.setRequestProperty(header.getKey(), header.getValue());
        }

        return new InputStreamReader(urlConnection.getInputStream(),
                                     detectCharset(urlConnection.getContentType(), charset));
    }

    public static Reader reader(URLConnection connection) throws IOException
    {
        return new InputStreamReader(connection.getInputStream(), detectCharset(connection.getContentType()));
    }

    public static Charset detectCharset(final String contentType)
    {
        return detectCharset(contentType, DEFAULT_CHARSET);
    }

    public static Charset detectCharset(final String contentType, Charset defaultCharset)
    {
        if (contentType != null) {
            Matcher m = CHARSET_PATTERN.matcher(contentType);
            if (m.matches() && m.groupCount() > 0) {
                return Charset.forName(m.group(1));
            }
        }

        return defaultCharset;
    }

    private static URLConnection openConnection(String url)
            throws IOException
    {
        return new URL(url).openConnection();
    }

}

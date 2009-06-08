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

package org.bc.iterate.net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Urls
{
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String ACCEPT_CHARSET_HEADER = "accept-encoding";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("^.*?;\\s*charset\\s*\\=\\s*(\\S+).*$");

    public static Reader reader(String url, String... headers)
            throws IOException
    {
        final URLConnection urlConnection = new URL(url).openConnection();

        // Set any supplied headers
        for (String header : headers) {
            String[] nameAndValue = header.split(":");
            if (nameAndValue.length > 0) {
                urlConnection.setRequestProperty(nameAndValue[0].trim(), nameAndValue[1].trim());
            }
        }

        String charset = detectCharset(urlConnection.getContentType());
        return new InputStreamReader(urlConnection.getInputStream(),
                                     (charset == null) ? DEFAULT_CHARSET : charset);
    }

    protected static String detectCharset(final String contentType)
    {
        if (contentType != null) {
            Matcher m = CHARSET_PATTERN.matcher(contentType);
            if (m.matches() && m.groupCount() > 0) {
                return m.group(1);
            }
        }

        return DEFAULT_CHARSET;
    }

    public static Reader reader(String url, Charset charset, String... headers)
            throws IOException
    {
        final URLConnection urlConnection = new URL(url).openConnection();

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

        return new InputStreamReader(urlConnection.getInputStream(), charset);
    }

    public static Reader reader(String url, Map<String, String> headers) throws IOException
    {
        final URLConnection urlConnection = new URL(url).openConnection();

        // Set any supplied headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            urlConnection.setRequestProperty(header.getKey(), header.getValue());
        }

        String charset = detectCharset(urlConnection.getContentType());
        return new InputStreamReader(urlConnection.getInputStream(),
                                     (charset == null) ? DEFAULT_CHARSET : charset);
    }

    public static Reader reader(String url, Charset charset, Map<String, String> headers)
            throws IOException
    {
        final URLConnection urlConnection = new URL(url).openConnection();

        // Set any supplied headers
        boolean needCharset = true;
        for (Map.Entry<String, String> header : headers.entrySet()) {
            final String name = header.getKey();
            urlConnection.setRequestProperty(name, header.getValue());
            if (ACCEPT_CHARSET_HEADER.equals(name.toLowerCase())) {
                needCharset = false;
            }
        }

        if (needCharset) {
            urlConnection.setRequestProperty("Accept-Charset", charset.name());
        }

        return new InputStreamReader(urlConnection.getInputStream(), charset);
    }

    public static Reader reader(URLConnection connection) throws IOException
    {
        return new InputStreamReader(connection.getInputStream(), detectCharset(connection.getContentType()));
    }

}

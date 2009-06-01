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

package org.bc.iterate;

import org.bc.iterate.function.ToString;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Strings
{
    private static String defaultJoinSeparator = ",";
    private static final int DEFAULT_JOIN_BUFFER_SIZE = 256;

    public static void setDefaultJoinSeparator(String separator)
    {
        defaultJoinSeparator = separator;
    }

    public static <X> String join(final Iterable<X> items)
    {
        return join(items, defaultJoinSeparator);
    }

    public static <X> String join(final Iterable<X> items, final String separator)
    {
        final StringBuilder builder = builder(DEFAULT_JOIN_BUFFER_SIZE);
        try {
            return join(items, builder, separator).toString();
        } catch (IOException ignored) {
            return builder.toString();
        }
    }

    public static <X, Y extends Appendable> Y join(final Iterable<X> items, Y result) throws IOException
    {
        return join(items, result, defaultJoinSeparator);
    }

    public static <X, Y extends Appendable> Y join(final Iterable<X> items, Y result, final String separator)
            throws IOException
    {
        return join(items, new ToString<X>(), result, separator);
    }

    public static <X> Function<X, String> toString()
    {
        return new ToString<X>();
    }

    public static <X, Y extends Appendable> Y join(final Iterable<X> items, Function<X, String> toString, Y result)
            throws IOException
    {
        return join(items, toString, result, defaultJoinSeparator);
    }

    public static <X, Y extends Appendable> Y join(final Iterable<X> items, final Function<X, String> toString,
                                                   final Y result, final String separator) throws IOException
    {
        Iterator<X> iter = items.iterator();
        if (iter.hasNext()) {
            result.append(toString.apply(iter.next()));
        }

        while (iter.hasNext()) {
            result.append(separator).append(toString.apply(iter.next()));
        }

        return result;
    }

    public static Function<String, String> lowerCase()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return s.toLowerCase();
            }
        };
    }

    public static Function<String, String> upperCase()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return s.toUpperCase();
            }
        };
    }

    public static Function<String, String> titleCase()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return smartTitleCase(s);
            }
        };
    }

    public static String titleCase(String s)
    {
        if(s == null || s.length() == 0) {
            return "";
        }

        String[] words = s.split("\\s+");
        if(words.length > 0)
        {
            StringBuilder result = new StringBuilder(s.length()).append(smartInitialCap(words[0]));

            int i = 1;
            for (; i < words.length - 1; i++) {
                final String word = words[i];
                result.append(' ').append(initialCap(word));
            }

            i = words.length - 1;
            if (words.length > 1) {
                result.append(' ').append(smartInitialCap(words[i]));
            }

            return result.toString();

        }

        return s;
    }

    public static String smartTitleCase(String s)
    {
        if(s == null || s.length() == 0) {
            return "";
        }
        
        String[] words = s.split("\\s+");
        if (words.length > 0) {
            StringBuilder result = new StringBuilder(s.length()).append(smartInitialCap(words[0]));

            int i = 1;
            for (; i < words.length - 1; i++) {
                final String word = words[i];
                result.append(' ').append(LOWERCASE_TITLE_WORDS.contains(word) ? word : initialCap(word));
            }

            i = words.length - 1;
            if (words.length > 1) {
                result.append(' ').append(smartInitialCap(words[i]));
            }

            return result.toString();
        }

        return s;
    }

    public static CharSequence smartInitialCap(String word)
    {
        return word.equals(word.toLowerCase()) ? initialCap(word) : word;
    }

    public static CharSequence initialCap(String word)
    {
        return new StringBuilder(word.length())
                .append(Character.toUpperCase(word.charAt(0)))
                .append(word.substring(1).toLowerCase());
    }

    public final static Set<String> LOWERCASE_TITLE_WORDS;
    static {
        LOWERCASE_TITLE_WORDS = new HashSet<String>(Arrays.asList("a", "an", "and", "as", "at", "be", "but", "en",
                                                                  "for", "if", "in", "is", "of", "on", "or", "to",
                                                                  "via", "v.", "vs."));
    }

    public static StringBuilder builder(int size)
    {
        return new StringBuilder(size);
    }

    public static StringBuilder builder(CharSequence initialValue)
    {
        return new StringBuilder(initialValue);
    }

    private Strings() {}
}

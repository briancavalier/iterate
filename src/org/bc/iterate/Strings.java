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

import org.bc.iterate.function.RegexReplace;
import org.bc.iterate.function.ToString;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Provides {@link Function}s, {@link Condition}s, {@link Visitor}s, and other utility methods for {@link String}.
 *
 * @author Brian Cavalier
 */
public class Strings
{
    private static String defaultJoinSeparator = ",";
    private static final int DEFAULT_JOIN_BUFFER_SIZE = 256;

    public static void setDefaultJoinSeparator(String separator)
    {
        defaultJoinSeparator = separator;
    }

    /**
     * @param items items to join together into one {@code String}
     *
     * @return a {@code String} from all items by invoking {@code toString} on each, and joining them together,
     *         separated by {@link Strings#defaultJoinSeparator}
     */
    public static <X> String join(final Iterable<X> items)
    {
        return join(items, defaultJoinSeparator);
    }

    /**
     * @param items     items to join together into one {@code String}
     * @param separator non-null String to place between each item in {@code items}
     *
     * @return a {@code String} from all items by invoking {@code toString} on each, and joining them together,
     *         separated by {@code separator}
     */
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

    public static Condition<String> contains(final String contained)
    {
        return new Condition<String>()
        {
            public boolean eval(String s)
            {
                return s.contains(contained);
            }
        };
    }

    public static Condition<String> contains(final char contained)
    {
        return new Condition<String>()
        {
            public boolean eval(String s)
            {
                return s.indexOf(contained) != -1;
            }
        };
    }

    /**
     * @return a {@link org.bc.iterate.Function} that will return the result of replacing all occurrences of {@code
     *         Pattern} with {@code replacement} on {@code x}
     */
    public static Function<String, String> replace(Pattern pattern, String replacement)
    {
        return new RegexReplace(pattern, replacement);
    }

    /**
     * @return a {@link org.bc.iterate.Function} that will return the result of replacing all occurrences of {@code
     *         Pattern} with {@code replacement} on {@code x}
     */
    public static Function<String, String> replace(String pattern, String replacement)
    {
        return new RegexReplace(Pattern.compile(pattern), replacement);
    }

    /**
     * @param target      {@code char} to be replaced
     * @param replacement {@code char} with which to replace {@code target}
     *
     * @return a {@link Function} that will replace all occurrences of {@code target} with {@code replacement} on {@code
     *         x}.
     */
    public static Function<String, String> replace(final char target, final char replacement)
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return s.replace(target, replacement);
            }
        };
    }

    public static Function<String, String> tr(final String targetChars, final String replacementChars)
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return tr(s, targetChars, replacementChars);
            }
        };
    }

    public static BinaryFunction<String, String, List<String>> split()
    {
        return new BinaryFunction<String, String, List<String>>()
        {
            public List<String> apply(String s, String separator)
            {
                return Arrays.asList(s.split(separator));
            }
        };
    }

    public static BinaryFunction<String, String, List<String>> split(final int limit)
    {
        return new BinaryFunction<String, String, List<String>>()
        {
            public List<String> apply(String s, String separator)
            {
                return Arrays.asList(s.split(separator, limit));
            }
        };
    }

    /**
     * A simplified version of the traditional unix {@code tr} command that will replace all occurrences in {@code s} of
     * {@code targetChars.charAt(0)} with {@code replacementChars.charAt(0)}, {@code targetChars.charAt(1)} with {@code
     * replacementChars.charAt(1)}, and so on.
     *
     * @param s           {@code String} on which to performance character replacement
     * @param targetChars {@link String} containing characters to replace
     *
     * @return replacementChars {@link String} containing corresponding characters with which to replace
     */
    public static String tr(String s, String targetChars, String replacementChars)
    {
        final StringBuilder buf = builder(s.length());
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            final int index = targetChars.indexOf(c);
            buf.append((index == -1) ? c : replacementChars.charAt(index));
        }

        return buf.toString();
    }


    /**
     * @return a {@link Function} that will return a lowercase version of {@code x}.
     */
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

    /**
     * @return a {@link Function} that will return an uppercase version of {@code x}.
     */
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

    /**
     * @return a {@link Function} that will return a smart title-cased version of {@code x}, as defined in {@link
     *         Strings#smartTitleCase(String)}.
     */
    public static Function<String, String> smartTitleCase()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return smartTitleCase(s);
            }
        };
    }

    /**
     * @return a {@link Function} that will capitalize all words in {@code x}.
     */
    public static Function<String, String> titleCase()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return titleCase(s);
            }
        };
    }

    public static String titleCase(String s)
    {
        if (s == null || s.length() == 0) {
            return "";
        }

        String[] words = s.split("\\s+");
        if (words.length > 0) {
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
        if (s == null || s.length() == 0) {
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

    private Strings()
    {
    }
}

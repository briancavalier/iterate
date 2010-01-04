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

package org.bc.iterate;

import org.bc.iterate.function.RegexReplace;

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
    public static final String DEFAULT_JOIN_SEPARATOR = ",";

    private static final int DEFAULT_JOIN_ITEM_SPACE = 32;

    /**
     * Joins the supplied {@code items}, with {@link #DEFAULT_JOIN_SEPARATOR} between them.
     *
     * @param items items to join together into one {@code String}
     *
     * @return a {@code String} of all items
     */
    public static <X> String join(final Iterable<X> items)
    {
        return join(items, DEFAULT_JOIN_SEPARATOR);
    }

    /**
     * Joins the supplied {@code items}, with {@code separator} between them.
     *
     * @param items     items to join together into one {@code String}
     * @param separator non-null String to place between each item in {@code items}
     *
     * @return a {@code String} of all items
     */
    public static <X> String join(final Iterable<X> items, final String separator)
    {
        final StringBuilder builder = builder(Iterate.estimateSize(items) + DEFAULT_JOIN_ITEM_SPACE);
        try {
            return join(items, builder, separator).toString();
        } catch (IOException ignored) {
            return "";
        }
    }

    /**
     * Appends the supplied {@code items} to {@code result}, invoking {@code toString()} on each, with {@link
     * #DEFAULT_JOIN_SEPARATOR} between them.
     *
     * @param items  items to join together into one {@code String}
     * @param result {@link Appendable} to which each item will be appended
     *
     * @return {@code result}, to which all items have been appended
     */
    public static <X, Y extends Appendable> Y join(final Iterable<X> items, Y result) throws IOException
    {
        return join(items, result, DEFAULT_JOIN_SEPARATOR);
    }

    /**
     * Appends the supplied {@code items} to {@code result}, invoking {@code toString()} on each, with {@code separator}
     * between them.
     *
     * @param items     items to join together into one {@code String}
     * @param result    {@link Appendable} to which each item will be appended
     * @param separator non-null String to place between each item in {@code items}
     *
     * @return {@code result}, to which all items have been appended
     */
    public static <X, Y extends Appendable> Y join(final Iterable<X> items, Y result, final String separator)
            throws IOException
    {
        return join(items, Strings.<X>string(), result, separator);
    }

    /**
     * Joins the supplied {@code items}, using the supplied {@link Function} to convert each item to a {@code String},
     * with {@link #DEFAULT_JOIN_SEPARATOR} between them.
     *
     * @param items    items to join together into one {@code String}
     * @param toString {@link Function} to use to convert each item to a {@code String}
     *
     * @return a {@code String} of all items
     */
    public static <X> String join(final Iterable<X> items, Function<X, String> toString)
            throws IOException
    {
        return join(items, toString, DEFAULT_JOIN_SEPARATOR).toString();
    }

    /**
     * Appends the supplied {@code items}, using the supplied {@link Function} to convert each item to a {@code String},
     * with {@link #DEFAULT_JOIN_SEPARATOR} between them.
     *
     * @param items    items to join together into one {@code String}
     * @param toString {@link Function} to use to convert each item to a {@code String}
     * @param result   {@link Appendable} to which each item will be appended
     *
     * @return {@code result}, to which all items have been appended
     */
    public static <X, Y extends Appendable> Y join(final Iterable<X> items, Function<X, String> toString, Y result)
            throws IOException
    {
        return join(items, toString, result, DEFAULT_JOIN_SEPARATOR);
    }

    /**
     * Joins the supplied {@code items}, using the supplied {@link Function} to convert each item to a {@code String},
     * with {@code separator} between them.
     *
     * @param items     items to join together into one {@code String}
     * @param toString  {@link Function} to use to convert each item to a {@code String}
     * @param separator non-null String to place between each item in {@code items}
     *
     * @return a {@code String} of all items
     */
    public static <X> StringBuilder join(final Iterable<X> items, Function<X, String> toString, String separator)
            throws IOException
    {
        return join(items, toString, Strings.builder(Iterate.estimateSize(items) * DEFAULT_JOIN_ITEM_SPACE), separator);
    }

    /**
     * Appends the supplied {@code items}, using the supplied {@link Function} to convert each item to a {@code String},
     * with {@code separator} between them.
     *
     * @param items     items to join together into one {@code String}
     * @param toString  {@link Function} to use to convert each item to a {@code String}
     * @param result    {@link Appendable} to which each item will be appended
     * @param separator non-null String to place between each item in {@code items}
     *
     * @return {@code result}, to which all items have been appended
     */
    public static <X, Y extends Appendable> Y join(final Iterable<X> items, final Function<X, String> toString,
                                                   final Y result, final String separator) throws IOException
    {
        if (items != null) {
            Iterator<X> iterator = items.iterator();
            if (iterator.hasNext()) {
                result.append(toString.apply(iterator.next()));
            }

            while (iterator.hasNext()) {
                result.append(separator).append(toString.apply(iterator.next()));
            }
        }
        return result;
    }

    /**
     * a {@link Condition} that returns {@code true} iff the {@link String} being evaluated contains {@code contained}.
     *
     * @param contained {@link String} to find
     *
     * @return a {@link Condition} that returns {@code true} iff the {@link String} being evaluated contains {@code
     *         contained}.
     */
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

    /**
     * a {@link Condition} that returns {@code true} iff the {@link String} being evaluated contains the supplied {@code
     * char}.
     *
     * @param contained {@code char} to find
     *
     * @return a {@link Condition} that returns {@code true} iff the {@link String} being evaluated contains the
     *         supplied {@code char}.
     */
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
     * a {@link Function} that returns its {@code String} argument.
     *
     * @return a {@link Function} that returns its {@code String} argument.
     */
    public static Function<String, String> identity()
    {
        return Functions.identity();
    }

    /**
     * a {@link org.bc.iterate.Function} that returns the result of invoking {@code toString()} on {@code x}.
     *
     * @return a {@link org.bc.iterate.Function} that returns the result of invoking {@code toString()} on {@code x}.
     */
    public static <X> Function<X, String> string()
    {
        // IntelliJ flags this as invalid, but it is legal, and compiles/works.
        return (Function<X, String>) ToString.INSTANCE;
    }

    private enum ToString implements Function<Object, String>
    {
        INSTANCE;

        public String apply(Object o)
        {
            return o.toString();
        }
    }

    /**
     * a {@link org.bc.iterate.Function} that will return the result of replacing all occurrences of {@code Pattern}
     * with {@code replacement} on {@code x}
     *
     * @param pattern     {@link Pattern} to search for
     * @param replacement {@link String} to replace each occurrence of {@code pattern} with
     *
     * @return a {@link org.bc.iterate.Function} that will return the result of replacing all occurrences of {@code
     *         Pattern} with {@code replacement} on {@code x}
     */
    public static Function<String, String> replace(Pattern pattern, String replacement)
    {
        return new RegexReplace(pattern, replacement);
    }

    /**
     * a {@link org.bc.iterate.Function} that will return the result of replacing all occurrences of {@code Pattern}
     * with {@code replacement} on {@code x}
     *
     * @param pattern     {@link Pattern} to search for
     * @param replacement {@link String} to replace each occurrence of {@code pattern} with
     *
     * @return a {@link org.bc.iterate.Function} that will return the result of replacing all occurrences of {@code
     *         Pattern} with {@code replacement} on {@code x}
     */
    public static Function<String, String> replace(String pattern, String replacement)
    {
        return new RegexReplace(Pattern.compile(pattern), replacement);
    }

    /**
     * a {@link Function} that will replace all occurrences of {@code target} with {@code replacement} on {@code x}.
     *
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
            public String apply(final String s)
            {
                return s.replace(target, replacement);
            }
        };
    }


    /**
     * a {@link Function} that will trim leading and trailing whitespace from {@code x}.
     *
     * @return a {@link Function} that will trim leading and trailing whitespace from {@code x}.
     */
    public static Function<String, String> trim()
    {
        return StringTrim.INSTANCE;
    }

    private enum StringTrim implements Function<String, String>
    {
        INSTANCE;

        @Override
        public String apply(String s)
        {
            return s.trim();
        }

        @Override
        public String toString()
        {
            return "trim";
        }
    }

    /**
     * a {@link Function} that will perform a <a href="http://en.wikipedia.org/wiki/Tr_(Unix)">unix-style tr</a> on its
     * argument.
     *
     * @param targetChars      chars to be replaced
     * @param replacementChars corresponding chars to use as replacements.
     *
     * @return a {@link Function} that will perform a <a href="http://en.wikipedia.org/wiki/Tr_(Unix)">unix-style tr</a>
     *         on its argument.
     */
    public static Function<String, String> tr(final String targetChars, final String replacementChars)
    {
        return new Function<String, String>()
        {
            public String apply(final String s)
            {
                return tr(s, targetChars, replacementChars);
            }
        };
    }

    /**
     * a {@link Function} that will split its argument into tokens using the supplied {@code separator}
     *
     * @param separator {@code String} separator to use to split
     *
     * @return a {@link Function} that will split its argument into tokens using the supplied {@code separator}
     */
    public static Function<String, List<String>> split(final String separator)
    {
        return new Function<String, List<String>>()
        {
            public List<String> apply(final String s)
            {
                return Arrays.asList(s.split(separator));
            }
        };
    }

    /**
     * a {@link Function} that will split its argument into at most {@code limit} tokens using the supplied {@code
     * separator}
     *
     * @param {@code String} separator separator to use to split
     *
     * @return a {@link Function} that will split its argument into at most {@code limit} tokens using the supplied
     *         {@code separator}
     */
    public static Function<String, List<String>> split(final String separator, final int limit)
    {
        return new Function<String, List<String>>()
        {
            public List<String> apply(final String s)
            {
                return Arrays.asList(s.split(separator, limit));
            }
        };
    }

    /**
     * a {@link Function} that will split its argument into tokens using the supplied {@code separator}. Note that
     * this {@link Function} uses the faster {@link Strings#split(String, char)} helper rather than the slower,
     * regex-based {@link String#split(String)}
     *
     * @param separator {@code char} separator to use to split
     *
     * @return a {@link Function} that will split its argument into tokens using the supplied {@code separator}
     */
    public static Function<String, List<String>> split(final char separator)
    {
        return new Function<String, List<String>>()
        {
            public List<String> apply(final String s)
            {
                return split(s, separator);
            }
        };
    }

    /**
     * a {@link Function} that will split its argument into tokens using the supplied {@code separator}. Note that
     * this {@link Function} uses the faster {@link Strings#split(String, char)} helper rather than the slower,
     * regex-based {@link String#split(String)}
     *
     * @param separator {@code char} separator to use to split
     * @param limit     {@code String}s will be split into at most this many tokens
     *
     * @return a {@link Function} that will split its argument into tokens using the supplied {@code separator}
     */
    public static Function<String, List<String>> split(final char separator, final int limit)
    {
        return new Function<String, List<String>>()
        {
            public List<String> apply(final String s)
            {
                return split(s, separator, limit);
            }
        };
    }

    /**
     * a {@link Function} that returns the {@link String#length()} of its argument
     *
     * @return a {@link Function} that returns the {@link String#length()} of its argument
     */
    public static Function<String, Integer> length()
    {
        return StringLength.INSTANCE;
    }

    private enum StringLength implements Function<String, Integer>
    {
        INSTANCE;

        @Override
        public Integer apply(String s)
        {
            return s.length();
        }
    }

    /**
     * a {@link BinaryFunction} that counts occurrences of a {@code char} in a {@code String}
     *
     * @return a {@link BinaryFunction} that counts occurrences of a {@code char} in a {@code String}
     */
    public static BinaryFunction<String, Character, Integer> count()
    {
        return CharacterCount.INSTANCE;
    }

    private enum CharacterCount implements BinaryFunction<String, Character, Integer>
    {
        INSTANCE;

        @Override
        public Integer apply(String s, Character c)
        {
            return count(s, c);
        }
    }

    /**
     * @param beforeAndAfter {@code String} to prepend and append
     *
     * @return a {@link org.bc.iterate.Function} that will return {@code beforeAndAfter + x + beforeAndAfter}
     */
    public static Function<String, String> surround(final String beforeAndAfter)
    {
        return surround(beforeAndAfter, beforeAndAfter);
    }

    /**
     * @param before {@code String} to prepend
     * @param after  {@code String} to append
     *
     * @return a {@link org.bc.iterate.Function} that will return {@code before + x + after}
     */
    public static Function<String, String> surround(final String before, final String after)
    {
        return new Function<String, String>()
        {
            public String apply(final String s)
            {
                return before + s + after;
            }
        };
    }

    /**
     * Splits {@code s} into at most {@code limit} tokens.
     *
     * @param s         {@code String} to split
     * @param separator {@code s} will be split on occurrences of this {@code char}
     * @param limit     return at most this many tokens
     *
     * @return a {@link List} containing at most {@code limit} tokens
     */
    public static List<String> split(final String s, final char separator, final int limit)
    {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be >= 1");
        } else if (limit == 1) {
            return Arrays.asList(s);
        }

        List<String> results = new ArrayList<String>(limit);
        final int len = s.length();

        int i = 0;
        while (i < len && separator == s.charAt(i)) {
            i++;
        }

        int start = i;
        int last = limit - 1;
        while (i < len && results.size() < last) {
            final char c = s.charAt(i);
            if (c == separator) {
                results.add(s.substring(start, i));
                i++;
                while (i < len && separator == s.charAt(i)) {
                    i++;
                }
                start = i;
            }
            i++;
        }

        if (start < len && results.size() < limit) {
            results.add(s.substring(start, len));
        }
        return results;
    }

    /**
     * Splits {@code s} into tokens.
     *
     * @param s         {@code String} to split
     * @param separator {@code s} will be split on occurrences of this {@code char}
     *
     * @return a {@link List} containing tokens
     */
    public static List<String> split(final String s, final char separator)
    {
        // Estimate number of tokens.
        // Assume at least 2, and then assume a "word" length average of 5 characters.
        return split(2 + (s.length() / 5), s, separator);
    }

    /**
     * Splits {@code s} into tokens.
     *
     * @param estimatedTokens an estimate of the number of resulting tokens, to aid in efficient memory allocation. This
     *                        does not dictate or limit the number of tokens returns--it is <em>only</em> used to aid
     *                        memory allocation.
     * @param s               {@code String} to split
     * @param separator       {@code s} will be split on occurrences of this {@code char}
     *
     * @return a {@link List} containing tokens
     */
    public static List<String> split(int estimatedTokens, final String s, final char separator)
    {
        // Estimate number of tokens.  Assume at least 2, and then assume a "word" length average of
        // 5 characters.
        final int len = s.length();
        List<String> results = new ArrayList<String>(estimatedTokens);

        int i = 0;
        while (i < len && separator == s.charAt(i)) {
            i++;
        }

        int start = i;
        for (; i < len; i++) {
            final char c = s.charAt(i);
            if (c == separator) {
                results.add(s.substring(start, i));
                i++;
                while (i < len && separator == s.charAt(i)) {
                    i++;
                }
                start = i;
            }
        }

        if (start < len) {
            results.add(s.substring(start, len));
        }
        return results;
    }

    /**
     * Counts the occurrences of {@code c} in {@code s}
     *
     * @param s {@code String} in which to count
     * @param c {@code char} to count
     *
     * @return number of occurrences of {@code c} in {@code s}
     */
    public static int count(final String s, final char c)
    {
        final int len = s.length();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }

        return count;
    }

    /**
     * A simplified version of the traditional unix {@code tr} command that will replace all occurrences in {@code s} of
     * {@code targetChars.charAt(0)} with {@code replacementChars.charAt(0)}, {@code targetChars.charAt(1)} with {@code
     * replacementChars.charAt(1)}, and so on.
     *
     * @param s                {@link String} on which to performance character replacement
     * @param targetChars      {@link String} containing characters to replace
     * @param replacementChars {@link String} charaters to use as replacements for {@code targetChars}
     *
     * @return {@link String} containing corresponding characters with which to replace
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
     * {@link Function} that will return a lowercase version of {@code x}.
     *
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
     * {@link Function} that will return an uppercase version of {@code x}.
     *
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
     * {@link Function} that will return a smart title-cased version of {@code x}, as defined in {@link
     * Strings#smartTitleCase(String)}.
     *
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
     * {@link Function} that will capitalize all words in {@code x}.
     *
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

    /**
     * {@link Function} that will capitalize the first {@code char} in {@code x}.
     *
     * @return a {@link Function} that will capitalize the first {@code char} in {@code x}.
     */
    public static Function<String, String> capitalize()
    {
        return new Function<String, String>()
        {
            public String apply(String s)
            {
                return initialCap(s).toString();
            }

            @Override
            public String toString()
            {
                return "capitalize";
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
                result.append(' ').append(LOWERCASE_TITLE_WORDS.contains(word) ? word : smartInitialCap(word));
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
        LOWERCASE_TITLE_WORDS = new HashSet<String>(Arrays.asList("a", "an", "and", "as", "at", "be", "but", "by", "en",
                                                                  "for", "if", "in", "is", "nor", "of", "on", "or",
                                                                  "to",
                                                                  "via", "v.", "vs."));
    }

    /**
     * Creates a {@link StringBuilder} whose initial capacity is {@code size}
     *
     * @param size initial capacity
     *
     * @return a {@link StringBuilder} whose initial capacity is {@code size}
     */
    public static StringBuilder builder(int size)
    {
        return new StringBuilder(size);
    }

    /**
     * Creates a {@link StringBuilder} that contains a copy of {@code initialValue}
     *
     * @param initialValue initial value to copy
     *
     * @return a {@link StringBuilder} that contains a copy of {@code initialValue}
     */
    public static StringBuilder builder(CharSequence initialValue)
    {
        return new StringBuilder(initialValue);
    }

    private Strings()
    {
    }
}

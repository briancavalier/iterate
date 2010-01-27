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

import org.bc.iterate.function.RegexFind;
import org.bc.iterate.iterable.*;
import org.bc.iterate.net.Urls;
import org.bc.iterate.relational.JoinResult;
import org.bc.iterate.relational.JoinStrategy;
import org.bc.iterate.visitor.*;

import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The core class of the iterate package containing the core iteration methods {@code each}, {@code map}, {@code visit},
 * and {@code reduce}, as well as methods creating the most used {@link Visitor}s, {@link org.bc.iterate.Function}s, and
 * {@link org.bc.iterate.Condition}s.
 *
 * @author Brian Cavalier
 */
@SuppressWarnings({"ClassReferencesSubclass"})
public abstract class Iterate<X> implements Iterable<X>, HasEstimatedSize
{
    public static final int DEFAULT_ESTIMATED_SIZE = 128;
    private int estimatedSize;

    /**
     * Creates an {@link Iterate} that will operate on the items in the supplied {@link Iterable}.
     *
     * @param items {@link Iterable} containing items over which to iterate
     *
     * @return an {@link Iterate} that will operate on the items in the supplied {@link Iterable}.
     */
    public static <X> Iterate<X> each(final Iterable<X> items)
    {
        return new BasicIterateImpl<X>(items, estimateSize(items));
    }

    /**
     * Unfortunately necessary method that wraps an {@link java.util.Iterator} as an {@link Iterable}.  Ideally, this
     * would simply be another variant of {@code each()}, but that makes {@code each(Iterable)} and {@code
     * each(Iterator)} ambiguous.
     *
     * @param items {@link java.util.Iterator} to wrap as an {@link Iterable}
     *
     * @return {@link Iterable} whose {@link Iterable#iterator()} method will return {@code items}
     */
    public static <X> Iterate<X> each(final Iterator<X> items)
    {
        return each(Iterables.of(items));
    }

    /**
     * Creates an {@link Iterate} that will operate on the items in the supplied array.
     *
     * @param items array containing items over which to iterate
     *
     * @return an {@link Iterate} that will operate on the items in the supplied array.
     */
    public static <X> Iterate<X> each(final X... items)
    {
        return new BasicIterateImpl<X>(new ArrayIterable<X>(items), items.length);
    }

    /**
     * Creates an {@link Iterate} that will operate on the {@link java.util.Map.Entry} items in {@code map.entrySet()}.
     * Shortcut for {@code Iterate.each(map.entrySet())}
     *
     * @param map This {@link Map}'s {@link Map#entrySet()} will be used for iteration
     *
     * @return an {@link Iterate} that will operate on the {@link java.util.Map.Entry} items in {@code map.entrySet()}
     */
    public static <X, Y> Iterate<Map.Entry<X, Y>> each(final Map<X, Y> map)
    {
        return new BasicIterateImpl<Map.Entry<X, Y>>(map.entrySet(), map.size());
    }

    protected Iterate()
    {

    }

    protected Iterate(int estimatedSize)
    {
        this.estimatedSize = estimatedSize;
    }

    /**
     * Narrow the scope of iteration to items for which {@code c.eval(item) == true}
     *
     * @param c {@link Condition} to evaluate for each item.
     *
     * @return {@link Iterate} that will only iterate over items for which {@code c.eval(item) == true}
     */
    public Iterate<X> where(Condition<? super X> c)
    {
        return new BasicIterateImpl<X>(new FilterIterable<X>(this, c));
    }

    /**
     * Narrow the scope of iteration to items for which {@code c.compareTo(item) == 0}
     *
     * @param c {@link Comparable} to compare with each item.
     *
     * @return {@link Iterate} that will only iterate over items for which {@code c.compareTo(item) == 0}
     */
    public Iterate<X> like(Comparable<? super X> c)
    {
        return where(Conditions.eq(c));
    }

    /**
     * Limits iteration to all items before the first item for which {@code c.eval(item) == true}
     *
     * @param c {@link Condition} to evaluate for each item
     *
     * @return {@link Iterate} that will only iterate over items before the first for which {@code c.eval(item) ==
     *         true}
     */
    public Iterate<X> until(Condition<? super X> c)
    {
        return new BasicIterateImpl<X>(new UntilIterable<X>(this, c));
    }

    /**
     * Limits iteration to all items including and after the first item for which {@code c.eval(item) == true}
     *
     * @param c {@link Condition} to evaluate for each item
     *
     * @return {@link Iterate} that will only iterate over items including and after the first for which {@code
     *         c.eval(item) == true}
     */
    public Iterate<X> after(Condition<? super X> c)
    {
        return new BasicIterateImpl<X>(new AfterIterable<X>(this, c));
    }

    public Iterate<X> transform(Function<Iterable<X>, Iterable<X>> transformFunction)
    {
        return new BasicIterateImpl<X>(transformFunction.apply(this));
    }

    public <Y> Iterate<Y> map(Function<? super X, ? extends Y> f)
    {
        return new BasicIterateImpl<Y>(new FunctionalIterable<X, Y>(this, f));
    }

    public <Y, Z> Iterate<Y> map(BinaryFunction<? super X, ? super Z, Y> f, Z referenceData)
    {
        return new BasicIterateImpl<Y>(new BinaryFunctionalIterable<X, Y, Z>(this, referenceData, f));
    }

    /**
     * Uses the supplied {@link JoinStrategy} to perform a relational join on iterate items and {@code itemsToJoin}
     *
     * @param strategy    {@link JoinStrategy} that provides the actual join algorithm (e.g. left, right, inner, full,
     *                    etc.)
     * @param itemsToJoin right side items to join
     *
     * @return {@link Iterate} of {@link JoinResult}s containing joined pairs and join key that matched during
     *         application of the {@link org.bc.iterate.relational.JoinStrategy}
     */
    public <K, Y> Iterate<JoinResult<K, X, Y>> join(JoinStrategy<K, ? super X, ? super Y> strategy,
                                                    Iterable<Y> itemsToJoin)
    {
        //noinspection unchecked
        return new BasicIterateImpl<JoinResult<K, X, Y>>(((JoinStrategy<K, X, Y>) strategy).join(this, itemsToJoin));
    }

    public <V extends Visitor<? super X>> V visit(V visitor)
    {
        for (X x : this) {
            visitor.visit(x);
        }

        return visitor;
    }

    public <Y> Y visit(BinaryVisitor<? super X, ? super Y> visitor, Y parameter)
    {
        for (X x : this) {
            visitor.visit(x, parameter);
        }

        return parameter;
    }

    public <Y> Y reduce(BinaryFunction<? super X, ? super Y, ? extends Y> f, Y startingValue)
    {
        Y result = startingValue;
        for (X x : this) {
            result = f.apply(x, result);
        }

        return result;
    }

    public X reduce(BinaryFunction<? super X, ? super X, ? extends X> f)
    {
        final Iterator<X> i = this.iterator();
        if (i.hasNext()) {
            X result = i.next();
            while (i.hasNext()) {
                result = f.apply(i.next(), result);
            }

            return result;
        }

        return null;
    }

    /**
     * @deprecated will be removed before version 1.0
     */
    public <Y, Z> Z visit(TernaryVisitor<? super X, ? super Y, Z> visitor,
                          Function<? super X, ? extends Y> f,
                          Z param)
    {
        for (X x : this) {
            visitor.visit(x, f.apply(x), param);
        }

        return param;
    }

    /**
     * @param itemsToPrepend items to append
     *
     * @return a new {@link Iterate} that has the supplied items prepended to the iteration order.
     *
     * @deprecated use {@link #transform(Function)} plus {@link Iterables#prepend(Iterable)} instead
     */
    public Iterate<X> prepend(Iterable<X> itemsToPrepend)
    {
        return new BasicIterateImpl<X>(new ConcatIterable<X>(Arrays.asList(itemsToPrepend, this)));
    }

    /**
     * @param itemsToAppend items to append
     *
     * @return a new {@link Iterate} that has the supplied items appended to the iteration order.
     *
     * @deprecated use {@link #transform(Function)} plus {@link Iterables#append(Iterable)} instead
     */
    public Iterate<X> append(Iterable<X> itemsToAppend)
    {
        return each(new ConcatIterable<X>(Arrays.asList(this, itemsToAppend)));
    }

    /**
     * Limits iteration to the items between inclusive {@code start} and exclusive {@code end}
     *
     * @param start inclusive start index
     * @param end   exclusive end index
     *
     * @return a new {@link Iterate} that will only iterate over the items in the range from inclusive {@code start} to
     *         exclusive {@code end}.
     */
    public Iterate<X> slice(int start, int end)
    {
        return each(new SliceIterable<X>(this, start, end));
    }

    /**
     * Limits iteration to the items starting at inclusive {@code start}.
     *
     * @param start inclusive start index
     *
     * @return a new {@link Iterate} that will only iterate over the items starting at inclusive {@code start}.
     */
    public Iterate<X> slice(int start)
    {
        return each(new SliceIterable<X>(this, start));
    }

    public abstract Iterator<X> iterator();

    /**
     * Returns an {@link Iterable} which will group items in {@code items} into groups of size {@code groupSize} and
     * return each group as a {@code List}.  For example, if the supplied {@link Iterable} ({@code items}) has the
     * following 10 elements:
     * <pre>
     * [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
     * </pre>
     * and {@code groupSize == 3}, the new {@link Iterable} will have the following 4 elements (each element will be a
     * {@code List}) :
     * <pre>
     * [ [ 0, 1, 2 ], [ 3, 4, 5 ], [ 6, 7 ,8 ], [ 9 ] ]
     * </pre>
     *
     * @param items     items to group
     * @param groupSize size of each group
     *
     * @return an {@link Iterable} which will group items in {@code items} into groups of size {@code groupSize}.
     */
    public static <X> Iterate<Collection<X>> group(final Iterable<X> items, int groupSize)
    {
        return Iterate.each(new GroupIterable<X>(items, groupSize));
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable lines(InputStream in)
    {
        return lines(new InputStreamReader(in));
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable lines(InputStream in, Charset charset)
    {
        return lines(new InputStreamReader(in, charset));
    }

    public static LineReaderIterable lines(Reader reader)
    {
        return new LineReaderIterable(reader);
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable lines(String url, String... headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new FileReader(url));
        }
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable lines(String url, Charset charset, String... headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, charset, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new InputStreamReader(new FileInputStream(url), charset));
        }
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable lines(String url, Map<String, String> headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new FileReader(url));
        }
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable lines(String url, Charset charset, Map<String, String> headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, charset, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new InputStreamReader(new FileInputStream(url), charset));
        }
    }

    /**
     * an {@link Iterable} over {@code numBytesPerIteration}-size byte chunks read from {@code in}
     *
     * @param in                   {@link java.io.InputStream} from which to read bytes
     * @param numBytesPerIteration maximum number of bytes per chunk
     *
     * @return an {@link Iterable} over {@code numBytesPerIteration}-size byte chunks read from {@code in}
     */
    public static ByteBufferIterable bytes(InputStream in, int numBytesPerIteration)
    {
        return new ByteBufferIterable(in, numBytesPerIteration, true);
    }

    /**
     * an {@link Iterable} over {@code numBytesPerIteration}-size byte chunks read from {@code channel}
     *
     * @param channel              {@link java.nio.channels.ReadableByteChannel} from which to read bytes
     * @param numBytesPerIteration maximum number of bytes per chunk
     *
     * @return an {@link Iterable} over {@code numBytesPerIteration}-size byte chunks read from {@code channel}
     */
    public static ByteBufferIterable bytes(ReadableByteChannel channel, int numBytesPerIteration)
    {
        return new ByteBufferIterable(channel, numBytesPerIteration, true);
    }

    /**
     * an {@link Iterable} over all tokens from {@code in}, as delimited by {@code delimiter}
     *
     * @param in        {@link Readable} from which to read
     * @param delimiter delimiter used to split tokens from {@code in}
     *
     * @return an {@link Iterable} over all tokens from {@code in}, as delimited by {@code delimiter}
     */
    public static Iterate<String> tokens(Readable in, Pattern delimiter)
    {
        return each(new TokenizerIterable(in, delimiter));
    }

    /**
     * an {@link Iterable} over all tokens from {@code in}, as delimited by {@code delimiter}
     *
     * @param in        {@link Readable} from which to read
     * @param delimiter delimiter used to split tokens from {@code in}
     *
     * @return an {@link Iterable} over all tokens from {@code in}, as delimited by {@code delimiter}
     */
    public static Iterate<String> tokens(Readable in, String delimiter)
    {
        return tokens(in, Pattern.compile(delimiter));
    }

    /**
     * @param in    {@link Readable} from which to read data for matching
     * @param regex {@link Pattern} to match
     *
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterate<String> match(Readable in, Pattern regex)
    {
        return new RegexMatchIterable(in, regex);
    }

    /**
     * an {@link Iterable} over all substrings matching {@code regex}
     *
     * @param in    {@link Readable} from which to read data for matching
     * @param regex regular expression to match.
     *
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterable<String> match(Readable in, String regex)
    {
        return new RegexMatchIterable(in, Pattern.compile(regex));
    }

    /**
     * an {@link Iterable} over all substrings matching {@code regex}
     *
     * @param in    {@link String} from which to read data for matching
     * @param regex regular expression to match.
     *
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterate<String> match(String in, String regex)
    {
        return match(new StringReader(in), Pattern.compile(regex));
    }

    /**
     * an {@link Iterable} over all substrings matching {@code regex}
     *
     * @param in    {@link String} from which to read data for matching
     * @param regex regular expression to match.
     *
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterate<String> match(String in, Pattern regex)
    {
        return new RegexMatchIterable(new StringReader(in), regex);
    }

    //
    // Visitors
    //

    /**
     * a {@link BinaryVisitor} that will add the item {@code x} to the {@code Collection y}
     *
     * @return a {@link BinaryVisitor} that will add the item {@code x} to the {@code Collection y}
     */
    public static <X> BinaryVisitor<X, Collection> collect()
    {
        return new BinaryVisitor<X, Collection>()
        {
            public void visit(X x, Collection collection)
            {
                //noinspection unchecked
                collection.add(x);
            }
        };
    }

    /**
     * a {@link BinaryVisitor} that will add all elements of the {@link Iterable} {@code x} to the {@link Collection}
     * {@code y}
     *
     * @return a {@link BinaryVisitor} that will add all elements of the {@link Iterable} {@code x} to the {@link
     *         Collection} {@code y}
     */
    public static <X, IterableType extends Iterable<X>, CollectionType extends Collection<? super X>> BinaryVisitor<IterableType, CollectionType> addAll()
    {
        return new BinaryVisitor<IterableType, CollectionType>()
        {
            public void visit(IterableType iterable, CollectionType cx)
            {
                for (X x : iterable) {
                    cx.add(x);
                }
            }
        };
    }

    /**
     * @return a {@link BinaryVisitor} that will remove the item {@code x} from the {@code Collection y}
     */
    public static <X> BinaryVisitor<X, Collection> remove()
    {
        return new Remove<X>();
    }

    /**
     * @return a {@link TernaryVisitor} that will add the value {@code x} using the key {@code y} to the {@code Map z}
     *
     * @deprecated Use the {@link #map(Function)} with {@link #visit(BinaryVisitor, Object)} instead.
     */
    public static <X, Y> TernaryVisitor<X, Y, Map<Y, X>> map()
    {
        return new Mapper<X, Y>();
    }

    /**
     * a {@link BinaryVisitor} that will app the value {@code x} to a supplied {@link Map} using the key generated by
     * apply {@code mapFunction} to {@code x}
     *
     * @param mapFunction {@link Function} that will be used to generate map keys for each item
     *
     * @return a {@link BinaryVisitor} that will app the value {@code x} to a supplied {@link Map} using the key
     *         generated by apply {@code mapFunction} to {@code x}
     */
    public static <X, Y> BinaryVisitor<? super X, Map<Y, X>> map(final Function<X, Y> mapFunction)
    {
        return new BinaryVisitor<X, Map<Y, X>>()
        {
            public void visit(X x, Map<Y, X> yxMap)
            {
                yxMap.put(mapFunction.apply(x), x);
            }
        };
    }

    /**
     * @return a {@link BinaryVisitor} that will call {@code append(x)} on the {@link Appendable} {@code y}
     */
    public static <X> Append<X> append()
    {
        return new Append<X>();
    }

    /**
     * @param separator {@code String} separator that will be appended between each item.
     *
     * @return an {@link org.bc.iterate.visitor.AppendWithSeparator}
     */
    public static <X> AppendWithSeparator<X> append(final String separator)
    {
        return new AppendWithSeparator<X>(separator);
    }

    /**
     * @return a {@link BinaryVisitor} that will call {@code write(x)} on the {@link java.io.Writer} {@code y}
     */
    public static BinaryVisitor<String, Writer> write()
    {
        return new Write();
    }

    /**
     * @return a {@link BinaryVisitor} that will call {@code println(x)} on the {@link java.io.PrintStream} {@code y}
     */
    public static <X> BinaryVisitor<X, PrintStream> println()
    {
        return new PrintLine<X>();
    }

    //
    // Functions
    //

    /**
     * @param pattern {@link Pattern} to find
     *
     * @return a {@link Function} that will return the whole substring matched by {@code pattern}
     */
    public static Function<String, String> find(Pattern pattern)
    {
        return new RegexFind(pattern);
    }

    /**
     * @param pattern regex pattern to find--will be compiled to a {@link Pattern} once, for faster execution.
     *
     * @return a {@link Function} that will return the whole substring matched by {@code pattern}
     */
    public static Function<String, String> find(String pattern)
    {
        return new RegexFind(Pattern.compile(pattern));
    }

    /**
     * @param pattern {@link Pattern} to find
     * @param group   number of the regex group to return when {@code pattern} is matched.
     *
     * @return a {@link Function} that will return the specified {@code group} within the first substring matched by
     *         {@code pattern}
     */
    public static Function<String, String> find(Pattern pattern, int group)
    {
        return new RegexFind(pattern, group);
    }

    /**
     * @param pattern regex pattern to find--will be compiled to a {@link Pattern} once, for faster execution.
     * @param group   number of the regex group to return when {@code pattern} is matched.
     *
     * @return a {@link Function} that will return the specified {@code group} within the first substring matched by
     *         {@code pattern}
     */
    public static Function<String, String> find(String pattern, int group)
    {
        return new RegexFind(Pattern.compile(pattern), group);
    }

    // Experimental terminal shortcut methods.
    // These can be replicated easily using each().visit(), but these
    // may provide more convenience and shorter code in most cases.

    public <CollectionType extends Collection<? super X>> CollectionType add(CollectionType c)
    {
        return visit(collect(), c);
    }

    public List<X> list()
    {
        return add(new ArrayList<X>(estimateSize(this)));
    }

    public Set<X> set()
    {
        return add(new HashSet<X>(estimateSize(this)));
    }

    public <A extends Appendable> A append(A a) throws IOException
    {
        for (X x : this) {
            a.append(x.toString());
        }

        return a;
    }

    public StringBuilder append(StringBuilder b)
    {
        return visit(append(), b);
    }

    public PrintStream println(PrintStream p)
    {
        for (X x : this) {
            p.println(x);
        }

        return p;
    }

    public PrintWriter println(PrintWriter p)
    {
        for (X x : this) {
            p.println(x);
        }

        return p;
    }

    /**
     * Attempts to estimate the number of items in {@code items} by checking its type to see if it is a type with a
     * known size: <ul> <li>a {@link Collection} or {@link Map}: {@link Collection#size()} or {@link Map#size()}</li>
     * <li>an {@link Iterate}: {@link #estimatedSize}</li> <li>any other type of {@link Iterable}: {@link
     * #DEFAULT_ESTIMATED_SIZE}</li> <li>a {@link String}: {@link String#length()} <li>an array (e.g. int[], etc.):
     * {@link Array#getLength(Object)}</li> <li>{@code null}: {@code 0} <li>anything else: {@code 1} </ul>
     *
     * @param items thing whose size will be estimated
     *
     * @return estimated size of {@code items}, based on rules above.
     */
    @SuppressWarnings({"ChainOfInstanceofChecks"})
    public static int estimateSize(Object items)
    {
        if (items == null) {
            return 0;
        }

        int sizeEstimate = 1;
        if (items instanceof Collection) {
            sizeEstimate = ((Collection) items).size();
        } else if (items instanceof Map) {
            sizeEstimate = ((Map) items).size();
        } else if (items instanceof HasEstimatedSize) {
            sizeEstimate = ((HasEstimatedSize) items).getEstimatedSize();
        } else if (items instanceof Iterable) {
            // Just have to return something reasonable here
            sizeEstimate = DEFAULT_ESTIMATED_SIZE;
        } else if (items instanceof CharSequence) {
            sizeEstimate = ((CharSequence) items).length();
        } else if (items instanceof Object[]) {
            // Separate cases for Object[] and primitive arrays because instanceof is typically much faster
            // than Class.isArray(), but primitive arrays cannot be checked with instanceof.
            // i.e. int[] instanceof Object[] == false
            sizeEstimate = ((Object[]) items).length;
        } else if (items.getClass().isArray()) {
            sizeEstimate = Array.getLength(items);
        }
        return sizeEstimate;
    }

    @Override
    public int getEstimatedSize()
    {
        return estimatedSize;
    }
}

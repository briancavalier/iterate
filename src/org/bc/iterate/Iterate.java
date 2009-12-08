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

package org.bc.iterate;

import org.bc.iterate.function.RegexFind;
import org.bc.iterate.function.ToString;
import org.bc.iterate.iterable.*;
import org.bc.iterate.net.Urls;
import org.bc.iterate.relational.JoinResult;
import org.bc.iterate.relational.JoinStrategy;
import org.bc.iterate.visitor.*;

import java.io.*;
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
public class Iterate<X> implements Iterable<X>
{
    private static final int DEFAULT_SIZE_ESTIMATE = 128;

    /**
     * @param items {@link Iterable} containing items over which to iterate
     * @return an {@link Iterate} internal iterator that will operate on the items in the supplied {@link Iterable}.
     */
    public static <X> Iterate<X> each(final Iterable<X> items)
    {
        return new Iterate<X>(items, estimateSize(items));
    }

    @SuppressWarnings({"ChainOfInstanceofChecks"})
    public static int estimateSize(Object items)
    {
        int sizeEstimate = DEFAULT_SIZE_ESTIMATE;
        if (items instanceof Collection) {
            sizeEstimate = ((Collection) items).size();
        } else if (items instanceof Map) {
            sizeEstimate = ((Map) items).size();
        } else if (items instanceof Iterate) {
            sizeEstimate = ((Iterate) items).sizeEstimate;
        }
        return sizeEstimate;
    }

    /**
     * @param items array containing items over which to iterate
     * @return an {@link Iterate} internal iterator that will operate on the items in the supplied array.
     */
    public static <X> Iterate<X> each(final X... items)
    {
        return new Iterate<X>(new ArrayIterable<X>(items), items.length);
    }

    /**
     * Shortcut for {@code Iterate.each(map.entrySet())}
     *
     * @param map This {@link Map}'s {@link Map#entrySet()} will be used for iteration
     * @return an {@link Iterate} internal iterator that will operate on the {@link java.util.Map.Entry} items in {@code
     *         map.entrySet()}
     */
    public static <X, Y> Iterate<Map.Entry<X, Y>> each(final Map<X, Y> map)
    {
        return new Iterate<Map.Entry<X, Y>>(map.entrySet(), map.size());
    }

    public static <X, Y extends Collection<X>> IterateCollection<X, Y> wrap(Y collection)
    {
        return new IterateCollection<X, Y>(collection);
    }

    protected Iterable<X> iterable;

    private int sizeEstimate;

    protected Iterate(Iterable<X> items)
    {
        this.iterable = items;
    }

    protected Iterate(Iterable<X> items, int sizeEstimate)
    {
        this.iterable = items;
        this.sizeEstimate = sizeEstimate;
    }

    protected Iterate()
    {
        this(null);
    }

    /**
     * Narrow the scope to items for which {@code c.eval(item) == true}
     *
     * @param c {@link Condition} to evaluate for each item.
     * @return {@link Iterate} that will only iterate over items for which {@code c.eval(item) == true}
     */
    public Iterate<X> where(Condition<? super X> c)
    {
        return new Iterate<X>(new FilterIterable<X>(this, c));
    }

    /**
     * Narrow the scope to items for which {@code c.compareTo(item) == 0}
     *
     * @param c {@link Comparable} to compare with each item.
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
     * @return {@link Iterate} that will only iterate over items before the first for which {@code c.eval(item) ==
     *         true}
     */
    public Iterate<X> until(Condition<? super X> c)
    {
        return new Iterate<X>(new UntilIterable<X>(this, c));
    }

    /**
     * Limits iteration to all items including and after the first item for which {@code c.eval(item) == true}
     *
     * @param c {@link Condition} to evaluate for each item
     * @return {@link Iterate} that will only iterate over items including and after the first for which {@code
     *         c.eval(item) == true}
     */
    public Iterate<X> after(Condition<? super X> c)
    {
        return new Iterate<X>(new AfterIterable<X>(this, c));
    }

    public <Y> Iterate<Y> map(Function<? super X, ? extends Y> f)
    {
        return new Iterate<Y>(new FunctionalIterable<X, Y>(this, f));
    }

    public <Y, Z> Iterate<Y> map(BinaryFunction<? super X, ? super Z, Y> f, Z referenceData)
    {
        return new Iterate<Y>(new BinaryFunctionalIterable<X, Y, Z>(this, referenceData, f));
    }

    /**
     * Uses the supplied {@link JoinStrategy} to perform a relational join on iterate items and {@code itemsToJoin}
     *
     * @param strategy    {@link JoinStrategy} that provides the actual join algorithm (e.g. left, right, inner, full, etc.)
     * @param itemsToJoin right side items to join
     * @return {@link Iterate} of {@link JoinResult}s containing joined pairs and join key that matched during application
     *         of the {@link org.bc.iterate.relational.JoinStrategy}
     */
    public <K, Y> Iterate<JoinResult<K, X, Y>> join(JoinStrategy<K, ? super X, ? super Y> strategy, Iterable<Y> itemsToJoin)
    {
        return new Iterate<JoinResult<K, X, Y>>(((JoinStrategy<K, X, Y>) strategy).join(this, itemsToJoin));
    }

    public <V extends Visitor<? super X>> V visit(V visitor)
    {
        for (X x : this) {
            visitor.visit(x);
        }

        return visitor;
    }

    public <Y> Y visit(BinaryVisitor<? super X, ? super Y> visitor, Y param)
    {
        for (X x : this) {
            visitor.visit(x, param);
        }

        return param;
    }

    public <Y> Y reduce(BinaryFunction<? super X, ? super Y, ? extends Y> f, Y param)
    {
        Y result = param;
        for (X x : this) {
            result = f.apply(x, result);
        }

        return result;
    }

    public X reduce(BinaryFunction<? super X, ? super X, ? extends X> f)
    {
        final Iterator<X> iter = this.iterator();
        if (iter.hasNext()) {
            X result = iter.next();
            while (iter.hasNext()) {
                result = f.apply(iter.next(), result);
            }

            return result;
        }

        return null;
    }

    /**
     * @deprecated This may be removed before version 1.0
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
     * @return a new {@link Iterate} that has the supplied items prepended to the iteration order.
     */
    public Iterate<X> prepend(Iterable<X> itemsToPrepend)
    {
        return new Iterate<X>(new FlattenIterable<X>(Arrays.asList(itemsToPrepend, this)));
    }

    /**
     * @param itemsToAppend items to append
     * @return a new {@link Iterate} that has the supplied items appended to the iteration order.
     */
    public Iterate<X> append(Iterable<X> itemsToAppend)
    {
        return new Iterate<X>(new FlattenIterable<X>(Arrays.asList(this, itemsToAppend)));
    }

    /**
     * @param start inclusive start index
     * @param end   exclusive end index
     * @return a new {@link Iterate} that will only iterate over the items in the range from inclusive {@code start}
     *         to exclusive {@code end}.
     */
    public Iterate<X> slice(int start, int end)
    {
        return new Iterate<X>(new SliceIterable<X>(this, start, end));
    }

    /**
     * @param start inclusive start index
     * @return a new {@link Iterate} that will only iterate over the items starting at inclusive {@code start}.
     */
    public Iterate<X> slice(int start)
    {
        return new Iterate<X>(new SliceIterable<X>(this, start), Math.max(0, this.sizeEstimate - start));
    }

    /**
     * @return {@link Iterator} over items in this {@link Iterate} view.  Typically, callers will not use this method,
     *         but instead will use the internal iteration methods {@code each}, {@code map}, {@code visit}, and {@code
     *         reduce}.
     */
    public Iterator<X> iterator()
    {
        return iterable.iterator();
    }

    /**
     * @param items {@code Collection} of items for which to return a sorted view
     * @return a sorted view of {@code items}
     */
    public static <X extends Comparable<X>> Iterable<X> sorted(final Collection<X> items)
    {
        List<X> sorted = new ArrayList<X>(items);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * @param items      {@code Collection} of items for which to return a sorted view
     * @param comparator {@link java.util.Comparator} to use to sort the {@code items}
     * @return a sorted view of {@code items}
     */
    public static <X> Iterable<X> sorted(final Collection<X> items, final Comparator<X> comparator)
    {
        List<X> sorted = new ArrayList<X>(items);
        Collections.sort(sorted, comparator);
        return sorted;
    }

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
     * @return an {@link Iterable} which will group items in {@code items} into groups of size {@code groupSize}.
     */
    public static <X> Iterable<Collection<X>> group(final Iterable<X> items, int groupSize)
    {
        return new GroupIterable<X>(items, groupSize);
    }

    /**
     * Returns a flattened view of the nested {@link Iterable}s, such that calling next() after the last item of the
     * first nested {@link Iterable} yields the first item of the second {@link Iterable}, calling next() again yields
     * the second item of the second {@link Iterable}, and so on.
     *
     * @param nested 2D {@link Iterable} of {@link Iterable}
     */
    public static <X, Y extends Iterable<X>> Iterable<X> flatten(final Iterable<Y> nested)
    {
        return new FlattenIterable<X>(nested);
    }

    /**
     * Returns a flattened view of the nested {@link Iterable}s, such that calling next() after the last item of the
     * first nested {@link Iterable} yields the first item of the second {@link Iterable}, calling next() again yields
     * the second item of the second {@link Iterable}, and so on.
     *
     * @param nested array of {@link Iterable}
     */
    public static <X> Iterable<X> flatten(Iterable<X>... nested)
    {
        return new FlattenIterable<X>(Arrays.asList(nested));
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable line(InputStream in)
    {
        return line(new InputStreamReader(in));
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable line(InputStream in, Charset charset)
    {
        return line(new InputStreamReader(in, charset));
    }

    public static LineReaderIterable line(Reader reader)
    {
        return new LineReaderIterable(reader);
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable line(String url, String... headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new FileReader(url));
        }
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable line(String url, Charset charset, String... headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, charset, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new InputStreamReader(new FileInputStream(url), charset));
        }
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable line(String url, Map<String, String> headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new FileReader(url));
        }
    }

    @SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
    public static LineReaderIterable line(String url, Charset charset, Map<String, String> headers) throws IOException
    {
        try {
            return new LineReaderIterable(Urls.reader(url, charset, headers));
        } catch (MalformedURLException ignored) {
            return new LineReaderIterable(new InputStreamReader(new FileInputStream(url), charset));
        }
    }

    /**
     * @param in                   {@link java.io.InputStream} from which to read bytes
     * @param numBytesPerIteration maximum number of bytes per chunk
     * @return an {@link Iterable} over {@code numBytesPerIteration}-size byte chunks read from {@code in}
     */
    public static ByteBufferIterable bytes(InputStream in, int numBytesPerIteration)
    {
        return new ByteBufferIterable(in, numBytesPerIteration, true);
    }

    /**
     * @param channel              {@link java.nio.channels.ReadableByteChannel} from which to read bytes
     * @param numBytesPerIteration maximum number of bytes per chunk
     * @return an {@link Iterable} over {@code numBytesPerIteration}-size byte chunks read from {@code channel}
     */
    public static ByteBufferIterable bytes(ReadableByteChannel channel, int numBytesPerIteration)
    {
        return new ByteBufferIterable(channel, numBytesPerIteration, true);
    }

    /**
     * @param in        {@link Readable} from which to read
     * @param delimiter delimiter used to split tokens from {@code in}
     * @return an {@link Iterable} over all tokens from {@code in}, as delimited by {@code delimiter}
     */
    public static Iterable<String> token(Readable in, Pattern delimiter)
    {
        return new TokenizerIterable(in, delimiter);
    }

    /**
     * @param in        {@link Readable} from which to read
     * @param delimiter delimiter used to split tokens from {@code in}
     * @return an {@link Iterable} over all tokens from {@code in}, as delimited by {@code delimiter}
     */
    public static Iterable<String> token(Readable in, String delimiter)
    {
        return new TokenizerIterable(in, Pattern.compile(delimiter));
    }

    /**
     * @param in    {@link Readable} from which to read data for matching
     * @param regex {@link Pattern} to match
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterable<String> match(Readable in, Pattern regex)
    {
        return new RegexMatchIterable(in, regex);
    }

    /**
     * @param in    {@link Readable} from which to read data for matching
     * @param regex regular expression to match.
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterable<String> match(Readable in, String regex)
    {
        return new RegexMatchIterable(in, Pattern.compile(regex));
    }

    /**
     * @param in    {@link String} from which to read data for matching
     * @param regex regular expression to match.
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterable<String> match(String in, String regex)
    {
        return new RegexMatchIterable(new StringReader(in), Pattern.compile(regex));
    }

    /**
     * @param in    {@link String} from which to read data for matching
     * @param regex regular expression to match.
     * @return an {@link Iterable} over all substrings matching {@code regex}
     */
    public static Iterable<String> match(String in, Pattern regex)
    {
        return new RegexMatchIterable(new StringReader(in), regex);
    }

    //
    // Visitors
    //

    /**
     * @return a {@link BinaryVisitor} that will add the item {@code x} to the {@code Collection y}
     */
    public static <X> BinaryVisitor<X, Collection> collect()
    {
        return new Collect<X>();
    }

    /**
     * @return a {@link BinaryVisitor} that will add all elements of the {@link Iterable} {@code x} to the {@link
     *         Collection} {@code y}
     */
    public static <X, IX extends Iterable<X>, CX extends Collection<? super X>> BinaryVisitor<IX, CX> flatten()
    {
        return new Flatten<IX, CX, X>();
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
     */
    public static <X, Y> TernaryVisitor<X, Y, Map<Y, X>> map()
    {
        return new Mapper<X, Y>();
    }

    /**
     * @return a {@link BinaryVisitor} that will call {@code append(x)} on the {@link Appendable} {@code y}
     */
    public static <X> Append<X> append()
    {
        return new Append<X>();
    }

    /**
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

    public static <X, Y> Visitor<X> bind(Y param, BinaryVisitor<X, Y> callee)
    {
        return new BindParam<X, Y>(param, callee);
    }

    public static <X, Y> BinaryVisitor<X, Y> unbind(Visitor<X> callee)
    {
        return new UnbindParam<X, Y>(callee);
    }

    //
    // Functions
    //

    /**
     * @return a {@link Function} that returns the result of invoking {@code toString()} on {@code x}.
     */
    public static <X> Function<X, String> toString()
    {
        return new ToString<X>();
    }

    /**
     * @return a {@link Function} that will return the whole substring matched by {@code pattern}
     */
    public static Function<String, String> find(Pattern pattern)
    {
        return new RegexFind(pattern);
    }

    /**
     * @return a {@link Function} that will return the whole substring matched by {@code pattern}
     */
    public static Function<String, String> find(String pattern)
    {
        return new RegexFind(Pattern.compile(pattern));
    }

    /**
     * @return a {@link Function} that will return the specified {@code group} within the first substring matched by
     *         {@code pattern}
     */
    public static Function<String, String> find(Pattern pattern, int group)
    {
        return new RegexFind(pattern, group);
    }

    /**
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

    public <C extends Collection<? super X>> C add(C c)
    {
        return visit(collect(), c);
    }

    public List<X> list()
    {
        return add(new ArrayList<X>(estimateSize(iterable)));
    }

    public Set<X> set()
    {
        return add(new HashSet<X>(estimateSize(iterable)));
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
}

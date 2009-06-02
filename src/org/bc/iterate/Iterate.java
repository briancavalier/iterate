/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate;

import org.bc.iterate.function.RegexFind;
import org.bc.iterate.function.RegexReplace;
import org.bc.iterate.function.ToString;
import org.bc.iterate.iterable.*;
import org.bc.iterate.net.Urls;
import org.bc.iterate.visitor.*;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

/**
 * The core class of the iterate package containing the core iterator methods ({@code each(...))}, and methods creating
 * the most used {@link Visitor}s, {@link org.bc.iterate.Function}s, and {@link org.bc.iterate.Condition}s.
 *
 * @author Brian Cavalier
 */
public class Iterate<X> implements Iterable<X>
{

    /**
     * @param items {@link Iterable} containing items over which to iterate
     *
     * @return an {@link Iterate} internal iterator that will operate on the items in the supplied {@link Iterable}.
     */
    public static <X> Iterate<X> each(final Iterable<X> items)
    {
        return new Iterate<X>(items);
    }

    /**
     * @param items array containing items over which to iterate
     *
     * @return an {@link Iterate} internal iterator that will operate on the items in the supplied array.
     */
    public static <X> Iterate<X> each(final X... items)
    {
        return new Iterate<X>(new ArrayIterable<X>(items));
    }

    /**
     * Shortcut for {@code Iterate.each(map.entrySet())}
     *
     * @param map This {@link Map}'s {@link Map#entrySet()} will be used for iteration  
     *
     * @return an {@link Iterate} internal iterator that will operate on the {@link java.util.Map.Entry} items in {@code
     *         map.entrySet()}
     */
    public static <X, Y> Iterate<Map.Entry<X, Y>> each(final Map<X, Y> map)
    {
        return each(map.entrySet());
    }

    protected Iterable<X> iterable;

    protected Iterate(Iterable<X> items)
    {
        this.iterable = items;
    }

    protected Iterate()
    {
        this(null);
    }

    /**
     * Narrow the scope to items for which {@code c.eval(item) == true}
     *
     * @param c {@link Condition} to evaluate for each item.
     *
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
        return new Iterate<X>(new UntilIterable<X>(this, c));
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
        return new Iterate<X>(new AfterIterable<X>(this, c));
    }

    /**
     * Creates a chained {@link Iterate} by invoking {@code f.apply()} on each items.
     *
     * @param f {@link Function} to apply to all items
     *
     * @return new chained {@link Iterate}
     */
    public <Y> Iterate<Y> map(Function<? super X, ? extends Y> f)
    {
        return new Iterate<Y>(new FunctionalIterable<X, Y>(this, f));
    }

    /**
     * Creates a chained {@link Iterate} by invoking {@code f.apply(item,param)} on each items.
     *
     * @param f             {@link BinaryFunction} to apply to all items
     * @param referenceData additional context will be passed as the second param to {@code f.apply}
     *
     * @return new chained {@link Iterate}
     */
    public <Y, Z> Iterate<Y> map(BinaryFunction<? super X, ? super Z, Y> f, Z referenceData)
    {
        return new Iterate<Y>(new BinaryFunctionalIterable<X, Y, Z>(this, referenceData, f));
    }

    /**
     * Applies {@code predicate} to each item
     *
     * @param predicate {@link Visitor} to apply
     *
     * @return {@code predicate}
     */
    public <P extends Visitor<? super X>> P visit(P predicate)
    {
        for (X x : this) {
            predicate.visit(x);
        }

        return predicate;
    }

    /**
     * Applies {@code predicate.apply(item,param)} to each item
     *
     * @param visitor {@link BinaryVisitor} to apply
     * @param param   value to pass as second parameter to {@code predicate.apply()}
     *
     * @return {@code param}
     */
    public <Y> Y visit(BinaryVisitor<? super X, ? super Y> visitor, Y param)
    {
        for (X x : this) {
            visitor.visit(x, param);
        }

        return param;
    }

    /**
     * Applies {@code param = f.apply(item,param)} to each item, thus updating the value of param after each iteration.
     *
     * @param f     {@link BinaryFunction} to apply
     * @param param value to pass as second parameter to {@code f.apply()}
     *
     * @return {@code param}
     */
    public <Y> Y reduce(BinaryFunction<? super X, ? super Y, ? extends Y> f, Y param)
    {
        Y result = param;
        for (X x : this) {
            result = f.apply(x, result);
        }

        return result;
    }

    /**
     * Applies {@code predicate.apply(item,f.map(item), param}) to each item
     *
     * @param visitor {@link TernaryVisitor} to apply
     * @param f       {@link Function} to apply to each item to generate the second param passed to {@code
     *                predicate.apply()}
     * @param param   value to pass as third paramter to {@code predicate.apply()}
     *
     * @return {@code param}
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
     * @return {@link Iterator} over items in this {@link Iterate} view
     */
    public Iterator<X> iterator()
    {
        return iterable.iterator();
    }

    /**
     * @param items {@code Collection} of items for which to return a sorted view
     *
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
     *
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
     *
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

    /**
     * @param start first integer value
     * @param end   exclusive last integer value
     *
     * @return an {@link Iterable} which will iterate over the integers from {@code start} (inclusive) to {@code end}
     *         (exclusive)
     */
    public static Iterable<Integer> range(int start, int end)
    {
        return new IntegerRange(start, end);
    }

    /**
     * @param start first integer value
     * @param end   exclusive last integer value
     * @param step  integer distance to step between each iteration.  For example, if {@code start = 0}, {@code end =
     *              5}, {@code step = 2}, the resulting {@link Iterable} will have the following elements: {@code [ 0,
     *              2, 4 ]}
     *
     * @return an {@link Iterable} which will iterate over the integers from {@code start} (inclusive) to {@code end}
     *         (exclusive), stepping by {@code step}
     */
    public static Iterable<Integer> range(int start, int end, int step)
    {
        return new StepIntegerRange(start, end, step);
    }

    /**
     * @param end exclusive last integer value
     *
     * @return an {@link Iterable} which will iterate over the integers from {@code 0} (zero, inclusive) to {@code end}
     *         (exclusive)
     */
    public static Iterable<Integer> upto(int end)
    {
        return new IntegerRange(0, end);
    }

    /**
     * @param end  exclusive last integer value
     * @param step integer distance to step between each iteration.  For example, {@code end = 5}, {@code step = 2}, the
     *             resulting {@link Iterable} will have the following elements: {@code [ 0, 2, 4 ]}
     *
     * @return an {@link Iterable} which will iterate over the integers from {@code 0} (zero, inclusive) to {@code end}
     *         (exclusive), stepping by {@code step}
     */
    public static Iterable<Integer> upto(int end, int step)
    {
        return new StepIntegerRange(0, end, step);
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

    public static ByteBufferIterable bytes(InputStream in, int numBytesPerIteration)
    {
        return new ByteBufferIterable(in, numBytesPerIteration, true);
    }

    public static ByteBufferIterable bytes(ReadableByteChannel channel, int numBytesPerIteration)
    {
        return new ByteBufferIterable(channel, numBytesPerIteration, true);
    }

    public static Iterable<String> token(Readable in, Pattern delimiter)
    {
        return new TokenizerIterable(in, delimiter);
    }

    public static Iterable<String> token(Readable in, String delimiter)
    {
        return new TokenizerIterable(in, Pattern.compile(delimiter));
    }

    public static Iterable<String> match(Readable in, Pattern regex)
    {
        return new RegexMatchIterable(in, regex);
    }

    public static Iterable<String> match(Readable in, String regex)
    {
        return new RegexMatchIterable(in, Pattern.compile(regex));
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
    public static <X> BinaryVisitor<X, Appendable> append()
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
     * @return a {@link Function} that will return the result of replacing all occurrences of {@code Pattern} with
     *         {@code replacement} on {@code x}
     */
    public static Function<String, String> replace(Pattern pattern, String replacement)
    {
        return new RegexReplace(pattern, replacement);
    }

    /**
     * @return a {@link Function} that will return the result of replacing all occurrences of {@code Pattern} with
     *         {@code replacement} on {@code x}
     */
    public static Function<String, String> replace(String pattern, String replacement)
    {
        return new RegexReplace(Pattern.compile(pattern), replacement);
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
}

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

import org.bc.iterate.visitor.AppendWithSeparator;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.*;

@SuppressWarnings({"ImplicitNumericConversion"})
public class IterateTest
{
    @Test
    public void each1()
    {
        final List<String> test = Arrays.asList("a", "b", "c", "d");
        final Counter<String> counter = new Counter<String>();

        assertEquals(test.size(), Iterate.each(test).visit(counter).getCount());
    }

    @Test
    public void each2()
    {
        final List<String> test = Arrays.asList("a", "b", "a", "c", "a", "d");
        final Counter<String> counter = new Counter<String>();

        assertEquals(3, Iterate.each(test).like("a").visit(counter).getCount());
    }

    @Test
    public void each3()
    {
        final List<Integer> test = Arrays.asList(1, 2, 3);
        final StringBuilder results = new StringBuilder(10);
        Iterate.each(test).map(new ToString<Integer>()).visit(new Visitor<String>()
        {
            public void visit(String s)
            {
                results.append(s).append("+");
            }
        });

        assertEquals("1+2+3+", results.toString());
    }

    @Test
    public void each4()
    {
        final List<Integer> test = Arrays.asList(1, 2, 3);
        final StringBuilder results = new StringBuilder(10);
        Iterate.each(test).like(2).map(new ToString<Integer>()).visit(new Visitor<String>()
        {
            public void visit(String s)
            {
                results.append(s).append("+");
            }
        });

        assertEquals("2+", results.toString());
    }

    @Test
    public void each5()
    {
        Assert.assertEquals(Integer.valueOf(10), Iterate.each(Arrays.asList(1, 2, 3, 4)).reduce(new Sum(), 0));
        Assert.assertEquals(Integer.valueOf(20), Iterate.each(Arrays.asList(1, 2, 3, 4)).reduce(new Sum(), 10));
    }

    @Test
    public void each6()
    {
        final List<String> test = Arrays.asList("a", "b", "c", "d");

        final List<String> results = Iterate.each(test).visit(new BinaryVisitor<String, List<String>>()
        {
            public void visit(String s, List<String> strings)
            {
                strings.add(s);
            }
        }, new ArrayList<String>(10));
        
        assertEquals(test, results);
    }

    @Test
    public void each7()
    {
        final Map<String, Integer> test = new LinkedHashMap<String, Integer>(3);
        test.put("foo", 1);
        test.put("bar", 2);
        test.put("baz", 3);

        assertEquals("foo1bar2baz3", Iterate.each(test).visit(new BinaryVisitor<Map.Entry<String, Integer>, StringBuilder>()
        {
            public void visit(Map.Entry<String, Integer> stringIntegerEntry, StringBuilder buf)
            {
                buf.append(stringIntegerEntry.getKey()).append(stringIntegerEntry.getValue());
            }
        }, new StringBuilder(16)).toString());
    }

    private static File generateTempFile(String name, String content, String... charset) throws IOException
    {
        String[] prefixSuffix = name.split("\\.");

        File tmp = File.createTempFile(prefixSuffix[0], prefixSuffix[1]);
        OutputStreamWriter out = null;
        try {
            //noinspection IOResourceOpenedButNotSafelyClosed
            out = new OutputStreamWriter(new FileOutputStream(tmp), (charset.length > 0)
                                                                    ? Charset.forName(charset[0])
                                                                    : Charset.defaultCharset());
            out.write(content);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return tmp;
    }

    @Test
    public void line4() throws IOException
    {
        Iterable<String> lines = Iterate.line("http://briancavalier.blogspot.com", "Accept: text/html");
        assertTrue(lines.iterator().hasNext());

        StringBuilder buf = new StringBuilder(100);
        int i = 0;
        for (; i < 10; i++) {
            buf.append("test").append(String.valueOf(i)).append("\\n");
        }

        File tmp = generateTempFile("Iterate.txt", buf.toString());

        lines = Iterate.line(tmp.getAbsolutePath());
        for (String line : lines) {
            assertEquals(buf.toString(), line);
        }
        //noinspection ResultOfMethodCallIgnored
        tmp.delete();
    }

    @Test
    public void line5() throws IOException
    {
        final String utf8String = "te�st �\\n";
        File tmp = generateTempFile("Iterate.txt", utf8String, "UTF-8");
        int count = 0;
        for (String line : Iterate.line(tmp.getAbsolutePath(), Charset.forName("UTF-8"))) {
            assertEquals(utf8String, line);
            count++;
        }

        assertEquals(1, count);
    }

    @Test
    public void bind()
    {
        final List<String> test = Arrays.asList("a", "b", "c", "d");
        final List<String> results = new ArrayList<String>(10);
        Iterate.each(test).visit(Iterate.bind(results, new BinaryVisitor<String, List<String>>()
        {
            public void visit(String s, List<String> strings)
            {
                strings.add(s);
            }
        }));

        assertEquals(test.size(), results.size());
    }

    @Test
    public void unbind()
    {
        final List<String> test = Arrays.asList("a", "b", "c", "d");
        final List<String> results = new ArrayList<String>(10);
        final List<String> results2 = new ArrayList<String>(10);
        Iterate.each(test).visit(Iterate.unbind(new Visitor<String>()
        {
            public void visit(String s)
            {
                results.add(s);
            }
        }), results2);

        assertEquals(0, results2.size());
        assertFalse(test.equals(results2));
        assertEquals(test, results);
    }

    @Test
    public void collect()
    {
        final List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(expected, Iterate.each(expected).visit(Iterate.collect(), new ArrayList<Integer>(5)));
    }

    @Test
    public void remove()
    {
        assertEquals(Arrays.asList(1, 3, 5), Iterate.each(Arrays.asList(2, 4))
                .visit(Iterate.remove(), new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5))));
    }

    @Test
    public void append()
    {
        Appendable results =
                Iterate.each(Arrays.asList(1, 2, 3, 4, 5)).visit(Iterate.append(), new StringBuilder(5));
        assertEquals("12345", results.toString());
    }

    @Test
    public void append2()
    {
        final AppendWithSeparator<Object> a = Iterate.append().with(",");

        Appendable results = Iterate.each(Arrays.asList(1, 2, 3, 4, 5)).visit(a, new StringBuilder(5));
        assertEquals("1,2,3,4,5", results.toString());

        a.reset();

        results = Iterate.each(Arrays.asList(1, 2, 3, 4, 5)).visit(a, results);
        assertEquals("1,2,3,4,51,2,3,4,5", results.toString());
    }

    private static class Counter<X> implements Visitor<X>
    {
        private int count = 0;

        public void visit(X x)
        {
            count++;
        }

        public int getCount()
        {
            return count;
        }

        public void reset()
        {
            count = 0;
        }
    }

    private static class ToString<X> implements Function<X, String>
    {
        public String apply(X x)
        {
            return x.toString();
        }
    }

    private static class Sum implements BinaryFunction<Integer, Integer, Integer>
    {
        public Integer apply(Integer x, Integer y)
        {
            return y + x;
        }
    }
}
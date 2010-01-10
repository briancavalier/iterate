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

import org.bc.iterate.relational.JoinResult;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.*;

public class AlgorithmsTest
{
    @SuppressWarnings({"ImplicitNumericConversion"})
    @Test
    public void count()
    {
        final List<String> values = asList("a", "b", "a", "c", "a", "d", "e");
        assertEquals(3, Algorithms.count(values, Conditions.eq("a")));
        assertEquals(1, Algorithms.count(values, Conditions.eq("e")));
        assertEquals(0, Algorithms.count(values, Conditions.eq("f")));
    }

    @Test
    public void first()
    {
        final List<String> values = asList("a", "b", "a", "c", "a", "d", "e");
        assertEquals("c", Algorithms.first(values, Conditions.eq("c")));
        assertEquals("a", Algorithms.first(values, Conditions.eq("a")));
        assertEquals("e", Algorithms.first(values, Conditions.eq("e")));
        assertNull(Algorithms.first(values, Conditions.eq("f")));        
    }

    @Test
    public void one()
    {
        final List<String> values = asList("a", "b", "a", "c", "a", "d", "e");
        assertTrue(Algorithms.one(values, Conditions.eq("c")));
        assertTrue(Algorithms.one(values, Conditions.eq("a")));
        assertTrue(Algorithms.one(values, Conditions.eq("e")));
        assertFalse(Algorithms.one(values, Conditions.eq("f")));
    }

    @Test
    public void all()
    {
        final List<String> values = asList("a", "b", "a", "c", "a", "d", "e");
        assertTrue(Algorithms.all(values, Conditions.gte("a")));
        assertTrue(Algorithms.all(values, Conditions.lt("z")));
        assertTrue(Algorithms.all(values, Conditions.lte("e")));

        assertFalse(Algorithms.all(values, Conditions.gt("b")));
        assertFalse(Algorithms.all(values, Conditions.eq("a")));
        assertFalse(Algorithms.all(values, Conditions.eq("f")));
    }

    @Test
    public void partition1()
    {
        final List<Integer> values = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Map<Boolean, Collection<Integer>> partitioned = Algorithms.partition(values, new Condition<Integer>()
        {
            public boolean eval(Integer integer)
            {
                return integer % 2 == 0;
            }
        });

        assertEquals(asList(1, 3, 5, 7, 9), partitioned.get(false));
        assertEquals(asList(2, 4, 6, 8, 10), partitioned.get(true));
    }

    @Test
    public void partition2()
    {
        final List<Integer> values = asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> trueResults = new ArrayList<Integer>(5);
        List<Integer> falseResults = new ArrayList<Integer>(5);

        Algorithms.partition(values, trueResults, falseResults, new Condition<Integer>()
        {
            public boolean eval(Integer integer)
            {
                return integer % 2 == 0;
            }
        });

        assertEquals(asList(1, 3, 5, 7, 9), falseResults);
        assertEquals(asList(2, 4, 6, 8, 10), trueResults);
    }

    @SuppressWarnings({"ImplicitNumericConversion"})
    @Test
    public void partition3()
    {
        final List<Integer> values = asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Map<Integer, Collection<Integer>> partitioned = Algorithms.partition(values, new Function<Integer, Integer>()
        {
            public Integer apply(Integer integer)
            {
                return integer % 3;
            }
        });

        assertEquals(3, partitioned.size());
        assertEquals(asList(3, 6, 9), partitioned.get(0));
        assertEquals(asList(1, 4, 7), partitioned.get(1));
        assertEquals(asList(2, 5, 8), partitioned.get(2));
    }

    @Test
    public void select1()
    {
        final List<String> values = asList("a", "b", "a", "c", "a", "d", "e");
        assertEquals(asList("a", "a", "a"), Algorithms.select(values, Conditions.eq("a")));
    }

    @Test
    public void select2()
    {
        final List<String> values = asList("a", "b", "a", "c", "a", "d", "e");
        assertEquals(asList("a", "a", "a"), Algorithms.select(values, new ArrayList<String>(10), Conditions.eq("a")));
    }

    @Test
    public void map1()
    {
        final List<String> values = asList("a", "b", "c");
        final List<String> results =
                Algorithms.map(values, new ArrayList<String>(10), new Function<String, String>()
                {
                    public String apply(String s)
                    {
                        return s + "1";
                    }
                });
        assertEquals(asList("a1", "b1", "c1"), results);
    }

    @Test
    public void zip1()
    {
        // Should result in a JoinResult that looks like: [(0,1,7), (1,2,8), (2,3,9)]
        final List<JoinResult<Integer,Integer,Integer>> results = Algorithms.zip(asList(1, 2, 3), asList(7, 8, 9));
        final Iterator<JoinResult<Integer, Integer, Integer>> r = results.iterator();
        for(int i=0; i<3; i++) {
            final JoinResult<Integer, Integer, Integer> result = r.next();
            assertEquals(Integer.valueOf(i), result.getKey());
            assertEquals(Integer.valueOf(i+1), result.getX());
            assertEquals(Integer.valueOf(i+7), result.getY());
        }
    }

    @Test
    public void sortByExample1()
    {
        List<Integer> example = asList(1, 3, 5, 2, 4, 6);
        assertEquals(example, Algorithms.sortByExample(example, asList(6, 5, 4, 3, 2, 1)));
        assertEquals(asList(3, 5, 4), Algorithms.sortByExample(example, asList(5, 4, 3)));
        assertEquals(example, Algorithms.sortByExample(example, asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void sortByExample2()
    {
        List<Integer> example = asList(1, 3, 5, 2, 4, 6);
        assertEquals(example, Algorithms.sortByExample(example, Strings.<Integer>string(), asList(6, 5, 4, 3, 2, 1)));
        assertEquals(asList(3, 5, 4), Algorithms.sortByExample(example, Strings.<Integer>string(), asList(5, 4, 3)));
        assertEquals(example, Algorithms.sortByExample(example, Strings.<Integer>string(), asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    private class Person {
        private final String name;

        public Person(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Person person = (Person) o;

            return !(name != null ? !name.equals(person.name) : person.name != null);
        }

        @Override
        public int hashCode()
        {
            return name != null ? name.hashCode() : 0;
        }
    }

    @Test
    public void sortByExample3()
    {
        // We have the names of some people in the right order, but then we want to sort the actual Person objects
        // into the same order.  Providing a mapping function from the Person to his/her name is all we need.
        final Person huey = new Person("huey");
        final Person dewey = new Person("dewey");
        final Person louie = new Person("louie");
        List<String> example = asList("donald", huey.getName(), dewey.getName(), louie.getName());
        assertEquals(asList(huey, dewey, louie), Algorithms.sortByExample(example, asList(dewey, louie, huey), new Function<Person, String>()
        {
            public String apply(Person p)
            {
                return p.getName();
            }
        }));
    }

    @Test
    public void sortByExample4()
    {
        List<Integer> example = asList(1, 3, 5, 2, 4, 6);
        final List<String> expected = asList("1", "3", "5", "2", "4", "6");
        assertEquals(expected, Algorithms.sortByExample(example, Strings.<Integer>string(), asList("6", "5", "4", "3", "2", "1"), Functions.<String>identity()));
        assertEquals(asList("3", "5", "4"), Algorithms.sortByExample(example, Strings.<Integer>string(), asList("5", "4", "3"), Functions.<String>identity()));
        assertEquals(expected, Algorithms.sortByExample(example, Strings.<Integer>string(), asList("1", "2", "3", "4", "5", "6", "7", "8", "9"), Functions.<String>identity()));
    }
}

package org.bc.iterate;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * @author bcavalier
 */
public class IterablesTest
{
    @Test
    public void concat()
    {
        final List<String> src = Arrays.asList("a", "b", "c", "i", "j", "k", "c", "b", "a");
        final List<List<String>> nested = Arrays.asList(src.subList(0, 3), src.subList(3, 6), src.subList(6, 9));
        final List<String> results = new ArrayList<String>(10);
        for (String s : Iterables.concat(nested)) {
            results.add(s);
        }
        assertEquals(src, results);
    }

    @Test
    public void generate()
    {
        final List<Integer> results = new ArrayList<Integer>(10);
        for (Integer i : Iterables.generate(10, Integers.identity())) {
            results.add(i);
        }

        assertEquals(10, results.size());
    }

    @Test
    public void cycle()
    {
        final Iterator<Integer> cycle = Iterables.cycle(Arrays.asList(1, 2)).iterator();
        assertEquals(Integer.valueOf(1), cycle.next());
        assertEquals(Integer.valueOf(2), cycle.next());
        assertEquals(Integer.valueOf(1), cycle.next());
        assertEquals(Integer.valueOf(2), cycle.next());
        assertEquals(Integer.valueOf(1), cycle.next());
        assertEquals(Integer.valueOf(2), cycle.next());
    }
}

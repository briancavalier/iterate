package org.bc.iterate.iterable;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

/**
 * @author bcavalier
 */
public class ConcatIterableTest
{
    @Test
    public void next() throws Exception {
        List<Integer> src = asList(1, 2, 3, 4);
        Iterable<Integer> c = new ConcatIterable<Integer>(asList(src.subList(0, 2), new ArrayList<Integer>(0), src.subList(2, 4)));
        List<Integer> results = new ArrayList<Integer>(src.size());
        for (Integer i : c) {
            results.add(i);
        }

        assertEquals(src, results);

    }
}

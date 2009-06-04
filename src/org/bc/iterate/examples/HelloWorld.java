/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.examples;

import static org.bc.iterate.Iterate.append;
import static org.bc.iterate.Iterate.each;
import static org.bc.iterate.Strings.*;

public class HelloWorld
{
    public static void main(String[] args)
    {
        each("hell0", "foo", "bar", "w0rld!", "baz")
                .where(contains('0'))
                .map(replace('0', 'o'))
                .map(titleCase())
                .visit(append(" "), System.out).println();
    }
}

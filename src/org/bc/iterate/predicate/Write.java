/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.predicate;

import org.bc.iterate.BinaryPredicate;

import java.io.Writer;
import java.io.IOException;

public class Write implements BinaryPredicate<String, Writer>
{
    public void apply(String s, Writer writer)
    {
        try {
            writer.write(s);
        } catch (IOException e) {
            System.err.println(e); // FIXME: Need exception strategy here
        }
    }
}

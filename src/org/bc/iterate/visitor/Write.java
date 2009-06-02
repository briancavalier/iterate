/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.visitor;

import org.bc.iterate.BinaryVisitor;

import java.io.Writer;
import java.io.IOException;

public class Write implements BinaryVisitor<String, Writer>
{
    public void visit(String s, Writer writer)
    {
        try {
            writer.write(s);
        } catch (IOException e) {
            System.err.println(e); // FIXME: Need exception strategy here
        }
    }
}

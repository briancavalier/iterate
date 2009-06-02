/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.visitor;

import org.bc.iterate.BinaryVisitor;

import java.util.Collection;

public class Flatten<IX extends Iterable<X>, CX extends Collection<? super X>, X> implements BinaryVisitor<IX, CX>
{
    public void visit(IX iterable, CX cx)
    {
        for (X x : iterable) {
            cx.add(x);
        }
    }
}

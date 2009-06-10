/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.Monoid;

/**
 * The identity function--returns it's argument.
 *
 * @author Brian Cavalier
 */
public class Identity<X> implements Monoid<X>
{
    /**
     * @param x any parameter
     *
     * @return {@code x}
     */
    public X apply(X x)
    {
        return x;
    }
}

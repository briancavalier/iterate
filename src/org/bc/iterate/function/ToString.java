/*
 * Copyright (c) 2007-2008, mSpoke. All Rights Reserved.
 */
package org.bc.iterate.function;

import org.bc.iterate.Function;

public class ToString<X> implements Function<X, String>
{
    public String apply(X x)
    {
        return x.toString();
    }
}

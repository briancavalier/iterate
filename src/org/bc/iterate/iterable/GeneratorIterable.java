package org.bc.iterate.iterable;

import org.bc.iterate.Function;

/**
 * {@link Iterable} that produces a specified number items using a supplied {@link Function}.
 *  
 * @author Brian Cavalier
 */
public class GeneratorIterable<X> extends IterableBase<X>
{
    private final int end;
    private final Function<Integer, X> generator;
    private int index = 0;

    public GeneratorIterable(int n, Function<Integer, X> generator)
    {
        this.end = n;
        this.generator = generator;
    }

    public boolean hasNext()
    {
        return index < end;
    }

    public X next()
    {
        return generator.apply(index++);
    }
}

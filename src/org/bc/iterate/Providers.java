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

import java.util.Arrays;
import java.util.Iterator;

/**
 * Provides helper methods for working with {@link Providers}.
 *
 * @author Brian Cavalier
 */
public class Providers
{
    /**
     * @param items items to be provided
     * @return a {@link Provider} that will provide the items in {@code items}, in order, with each subsequent
     *         call to {@link Provider#get()}
     */
    public static <T> Provider<T> of(final Iterable<T> items)
    {
        final Iterator<T> i = items.iterator();
        return new Provider<T>()
        {
            public T get()
            {
                return i.hasNext() ? i.next() : null;
            }
        };
    }

    /**
     * @param item singleton item to be provided
     * @return a {@link Provider} whose {@link Provider#get()} will always return {@code item}
     */
    public static <T> Provider<T> of(final T item)
    {
        return new Provider<T>()
        {
            public T get()
            {
                return item;
            }
        };
    }

    /**
     * @param items items to be provided
     * @return a {@link Provider} that will provide the items in {@code items} in order.
     */
    public static <T> Provider<T> of(final T... items)
    {
        return of(Arrays.asList(items));
    }

    /**
     * @param original {@link Provider} to memoize
     * @return a {@link Provider} that will return a cached instance, which is acquired on the first call to
     *         {@link Provider#get()}
     */
    public static <T> Provider<T> memoize(final Provider<T> original)
    {
        return new Provider<T>()
        {
            private transient T cached = null;
            private transient boolean init = true;

            public T get()
            {
                if (init) {
                    cached = original.get();
                    init = false;
                }
                return cached;
            }
        };
    }

    public static <T, IterableType extends Iterable<Provider<T>>> Provider<T> firstAvailable(final IterableType providers)
    {
        return new Provider<T>()
        {
            public T get()
            {
                for (Provider<? extends T> provider : providers) {
                    T item = provider.get();
                    if (item != null) {
                        return item;
                    }
                }

                return null;
            }
        };
    }

    public static <T, IterableType extends Iterable<Provider<T>>> Provider<T> sequence(final IterableType providers)
    {
        final Iterator<Provider<T>> providerIterator = providers.iterator();
        return new Provider<T>()
        {
            private Provider<? extends T> current = null;

            public T get()
            {
                if (current == null && providerIterator.hasNext()) {
                    current = providerIterator.next();
                }

                T provided;
                while (current != null) {
                    provided = current.get();
                    // If current Provider returns null, assume it is exhausted and move to next
                    // else, return the value it provided
                    if (provided == null) {
                        current = providerIterator.next();
                    } else {
                        return provided;
                    }
                }

                return null;
            }
        };
    }

    public static <T, U> Provider<U> compose(final Function<? super T, U> function, final Provider<T> provider)
    {
        return new Provider<U>()
        {
            public U get()
            {
                return function.apply(provider.get());
            }
        };
    }

    public static <T> Provider<T> synchronize(final Provider<T> provider)
    {
        return new Provider<T>()
        {
            public T get()
            {
                synchronized (provider) {
                    return provider.get();
                }
            }
        };
    }
}

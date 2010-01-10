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

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@link Providers}
 *
 * @author Brian Cavalier
 */
public class ProvidersTest
{
    @Test
    public void of() throws Exception
    {
        assertExpected(Providers.of(1, 2, 3), 1, 2, 3);
        assertExpected(Providers.of(asList(1, 2, 3)), 1, 2, 3);

        final Provider<Integer> p = Providers.of(1);
        assertExpected(p, 1);
        assertExpected(p, 1);
    }

    @Test
    public void memoize() throws Exception
    {
        Provider<Integer> p = Providers.memoize(Providers.of(1, 2, 3, 4, 5));
        assertEquals(Integer.valueOf(1), p.get());
        assertEquals(Integer.valueOf(1), p.get());
        assertEquals(Integer.valueOf(1), p.get());
    }

    @Test
    public void firstAvailable() throws Exception
    {
        Provider<Integer> provider =
                Providers.firstAvailable(asList(Providers.of(1, 2, 3), Providers.of(4, 5), Providers.of(6)));
        assertExpected(provider, 1, 2, 3, 4, 5, 6);

        provider = Providers.firstAvailable(asList(Providers.of(1, null, 3), Providers.of(4, 5), Providers.of(6)));
        assertExpected(provider, 1, 4, 3, 5, 6);
    }

    @Test
    public void sequence() throws Exception
    {
        Provider<Integer> provider =
                Providers.sequence(asList(Providers.of(1, 2, 3), Providers.of(4, 5), Providers.of(6)));
        assertExpected(provider, 1, 2, 3, 4, 5, 6);

        provider = Providers.sequence(asList(Providers.of(1, null, 3), Providers.of(4, 5), Providers.of(6)));
        assertExpected(provider, 1, 4, 5, 6);
    }

    @Test
    public void compose() throws Exception
    {
        assertEquals("123", Providers.compose(Strings.<Integer>string(), Providers.of(123)).get());
    }

    private void assertExpected(Provider<Integer> provider, int... expected)
    {
        for (int i : expected) {
            assertEquals(Integer.valueOf(i), provider.get());
        }
    }
}

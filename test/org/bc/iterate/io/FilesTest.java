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
package org.bc.iterate.io;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.bc.iterate.Condition;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@SuppressWarnings({"ImplicitNumericConversion"})
public class FilesTest
{
    private static final String TMP_DIR = "/tmp/FilesTest";
    private Set<File> files;
    private File testDir;

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    @Before
    public void setUp() throws IOException
    {
        testDir = new File(TMP_DIR);
        if(!testDir.exists()) {
            testDir.mkdirs();
        }
        files = new LinkedHashSet<File>(10);
        for (int i = 0; i < 10; i++) {
            final File f = new File(TMP_DIR, "FilesTest" + i + ".tmp");
            //noinspection ResultOfMethodCallIgnored
            if(!f.exists()) {
                f.createNewFile();
            }
            files.add(f);
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    @After
    public void tearDown()
    {
        for (File file : files) {
            if(file.exists()) {
                file.delete();
            }
        }
        if(testDir.exists()) {
            testDir.delete();
        }
    }

    @Test
    public void testList() throws IOException
    {
        int i = 0;
        for (File file : Files.list(TMP_DIR)) {
            Assert.assertTrue(files.contains(file));
            i++;
        }

        Assert.assertEquals(10, i);
    }

    @Test
    public void testName()
    {
        final Condition<File> c = Files.name(".+[2468]\\.tmp$");
        int i = 0;
        for (File file : testDir.listFiles()) {
            if(c.eval(file)) {
                i++;
            }
        }

        Assert.assertEquals(4, i);
    }
}

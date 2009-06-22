/*
 * Copyright (c) 2007-2009 Brian Cavalier
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

import org.bc.iterate.Condition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Files
{
    public static Iterable<File> list(File dir) throws IOException
    {
        if (!dir.exists()) {
            throw new FileNotFoundException(dir.getName());
        } else if (!dir.isDirectory()) {
            throw new IOException(dir + " is a regular file, but should be a directory");
        } else if (!dir.canRead()) {
            throw new IOException(dir + " is not readable with current permissions");
        }

        return Arrays.asList(dir.listFiles());
    }

    public static Iterable<File> list(String dir) throws IOException
    {
        return list(new File(dir));
    }

    public static Condition<File> name(final String nameRegex)
    {
        return name(Pattern.compile(nameRegex));
    }

    public static Condition<File> name(final Pattern nameRegex)
    {
        return new Condition<File>()
        {
            public boolean eval(File file)
            {
                return nameRegex.matcher(file.getName()).matches();
            }
        };
    }

    public static Condition<File> extension(final String extension)
    {
        return new Condition<File>()
        {
            public boolean eval(File file)
            {
                return file.getName().endsWith('.' + extension);
            }
        };
    }
}

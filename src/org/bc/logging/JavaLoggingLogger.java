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

package org.bc.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author bcavalier
 */
public class JavaLoggingLogger implements org.bc.logging.Logger
{
    private final java.util.logging.Logger logger;

    public JavaLoggingLogger(java.util.logging.Logger logger)
    {
        this.logger = logger;
    }

    public void trace(String message, Object... args)
    {
        log(java.util.logging.Level.FINER, message, args);
    }

    public void debug(String message, Object... args)
    {
        log(java.util.logging.Level.FINE, message, args);
    }

    public void info(String message, Object... args)
    {
        log(java.util.logging.Level.INFO, message, args);
    }

    public void warn(String message, Object... args)
    {
        log(java.util.logging.Level.WARNING, message, args);
    }

    public void error(String message, Object... args)
    {
        log(java.util.logging.Level.SEVERE, message, args);
    }

    public void fatal(String message, Object... args)
    {
        log(java.util.logging.Level.SEVERE, message, args);
    }

    private void log(Level level, String message, Object... args)
    {
        if(level.intValue() <= logger.getLevel().intValue()) {
            LogRecord r = new LogRecord(level, message);
            if(args.length > 0) {
                r.setParameters(args);
                if(args[0] instanceof Throwable) {
                    r.setThrown((Throwable) args[0]);
                } else {
                    final int last = args.length - 1;
                    if (args.length > 1 && args[last] instanceof Throwable) {
                        r.setThrown((Throwable) args[last]);
                    }
                }
            }
            logger.log(r);
        }
    }
}

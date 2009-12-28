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
package org.bc.iterate.examples.xml;

import org.bc.iterate.Function;
import org.bc.iterate.Iterate;
import static org.bc.iterate.Iterate.each;
import org.bc.iterate.xml.XML;
import org.bc.iterate.xml.XPath;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;

public class ParseRSS
{
    public static void main(String[] args) throws IOException, JDOMException
    {
        each(XPath.elements("/rss/channel/item/*", XML.parse(args[0]))).map(new Function<Element, String>()
        {
            public String apply(Element element)
            {
                return element.getName() + ": " + element.getTextTrim();
            }
        }).visit(Iterate.println(), System.out);
    }
}

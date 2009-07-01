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
package org.bc.iterate.xml;

import org.bc.iterate.BinaryFunction;
import org.bc.iterate.Function;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.DOMBuilder;
import org.jdom.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class XML
{
    public static Document parse(String urlOrPath) throws IOException, JDOMException
    {
        SAXBuilder builder = new SAXBuilder();
        try {
            return builder.build(new URL(urlOrPath));
        } catch (MalformedURLException ignored) {
            final FileInputStream in = new FileInputStream(urlOrPath);
            try {
                return parse(in);
            } finally {
                in.close();
            }
        }
    }

    public static Document parse(InputStream input) throws IOException, JDOMException
    {
        return new SAXBuilder().build(input);        
    }

    public static Document from(org.w3c.dom.Document w3cDocument)
    {
        return new DOMBuilder().build(w3cDocument);
    }

    public static Function<Element, List<Element>> children()
    {
        return new Function<Element, List<Element>>()
        {
            public List<Element> apply(Element element)
            {
                return children(element);
            }
        };
    }

    public static BinaryFunction<Element, String, List<Element>> childrenByName()
    {
        return new BinaryFunction<Element, String, List<Element>>()
        {
            public List<Element> apply(Element element, final String childElementName)
            {
                return children(element, childElementName);
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    public static List<Element> children(Element parent)
    {
        return parent.getChildren();
    }

    @SuppressWarnings({"unchecked"})
    public static List<Element> children(Element parent, String childElementName)
    {
        return parent.getChildren(childElementName);
    }

}

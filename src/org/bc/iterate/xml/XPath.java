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

import org.jdom.*;

import java.util.List;

public class XPath
{
    public static List<Element> elements(org.jdom.xpath.XPath xpath, Parent startingNode) throws JDOMException
    {
        //noinspection unchecked
        return nodes(xpath, startingNode);
    }

    public static List<Element> elements(String xpathExpression, Parent startingNode) throws JDOMException
    {
        //noinspection unchecked
        return nodes(org.jdom.xpath.XPath.newInstance(xpathExpression), startingNode);
    }

    public static List<Attribute> attributes(org.jdom.xpath.XPath xpath, Parent startingNode) throws JDOMException
    {
        //noinspection unchecked
        return nodes(xpath, startingNode);
    }

    public static List<Attribute> attributes(String xpathExpression, Parent startingNode) throws JDOMException
    {
        //noinspection unchecked
        return nodes(org.jdom.xpath.XPath.newInstance(xpathExpression), startingNode);
    }

    public static List<Text> text(org.jdom.xpath.XPath xpath, Parent startingNode) throws JDOMException
    {
        //noinspection unchecked
        return nodes(xpath, startingNode);
    }

    public static List<Text> text(String xpathExpression, Parent startingNode) throws JDOMException
    {
        //noinspection unchecked
        return nodes(org.jdom.xpath.XPath.newInstance(xpathExpression), startingNode);
    }

    public static List nodes(org.jdom.xpath.XPath xpath, Parent startingNode) throws JDOMException
    {
        return xpath.selectNodes(startingNode);
    }
}
